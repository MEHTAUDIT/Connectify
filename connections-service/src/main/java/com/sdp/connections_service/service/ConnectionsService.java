package com.sdp.connections_service.service;

import com.sdp.connections_service.auth.UserContextHolder;
import com.sdp.connections_service.clients.UsersClient;
import com.sdp.connections_service.dto.UserDto;
import com.sdp.connections_service.entity.Person;
import com.sdp.connections_service.event.AcceptConnectionRequestEvent;
import com.sdp.connections_service.event.SendConnectionRequestEvent;
import com.sdp.connections_service.exception.ResourceNotFoundException;
import com.sdp.connections_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j

public class ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptRequestKafkaTemplate;
    private final UsersClient usersClient;

    public List<Person> getFirstDegreeConnections(@RequestHeader("X-User-Id") Long userId) {

        if(!CheckUserExists(userId)){
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        addPerson(userId);

        log.info("Getting first degree connections for user with id: {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    public Boolean sendConnectionRequest(Long receiverId) {

        Long senderId = UserContextHolder.getCurrentUserId();

        if(!CheckUserExists(senderId)){
            throw new ResourceNotFoundException("User not found with id: " + senderId);
        }

        if(!CheckUserExists(receiverId)){
            throw new ResourceNotFoundException("User not found with id: " + receiverId);
        }

        log.info("Sending connection request from user with id: {} to user with id: {}", senderId, receiverId);

        if(senderId.equals(receiverId)){
            throw new RuntimeException("Sender and receiver cannot be the same");
        }

        addPerson(senderId);
        addPerson(receiverId);

        boolean connectionRequestExists = personRepository.checkConnectionRequestExists(senderId, receiverId);

        if(connectionRequestExists){
            throw new RuntimeException("Connection request already exists");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);

        if(alreadyConnected){
            throw new RuntimeException("Users are already connected");
        }

        log.info("Adding connection request from user with id: {} to user with id: {}", senderId, receiverId);
        personRepository.addConnectionRequest(senderId, receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        log.info("Sending connection request from user with id: {} to user with id: {}", senderId, receiverId);
        sendRequestKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);
        return true;
    }

    public Boolean acceptConnectionRequest(Long senderId) {

        Long receiverId = UserContextHolder.getCurrentUserId();

        if(!CheckUserExists(senderId)){
            throw new ResourceNotFoundException("User not found with id: " + senderId);
        }

        if(!CheckUserExists(receiverId)){
            throw new ResourceNotFoundException("User not found with id: " + receiverId);
        }

        log.info("Accepting connection request from user with id: {} to user with id: {}", senderId, receiverId);

        if(senderId.equals(receiverId)){
            throw new RuntimeException("Sender and receiver cannot be the same");
        }

        addPerson(senderId);
        addPerson(receiverId);

        boolean connectionRequestExists = personRepository.checkConnectionRequestExists(senderId, receiverId);

        if(!connectionRequestExists){
            throw new RuntimeException("Connection request does not exist");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);

        if(alreadyConnected){
            throw new RuntimeException("Users are already connected");
        }

        log.info("Adding connection from user with id: {} to user with id: {}", senderId, receiverId);
        personRepository.acceptConnectionRequest(senderId, receiverId);

        log.info("Accepting connection request from user with id: {} to user with id: {}", senderId, receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptRequestKafkaTemplate.send("accept-connection-request-topic", acceptConnectionRequestEvent);
        return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {

        Long receiverId = UserContextHolder.getCurrentUserId();

        if(!CheckUserExists(senderId)){
            throw new ResourceNotFoundException("User not found with id: " + senderId);
        }

        if(!CheckUserExists(receiverId)){
            throw new ResourceNotFoundException("User not found with id: " + receiverId);
        }

        log.info("Rejecting connection request from user with id: {} to user with id: {}", senderId, receiverId);

        if(senderId.equals(receiverId)){
            throw new RuntimeException("Sender and receiver cannot be the same");
        }

        boolean connectionRequestExists = personRepository.checkConnectionRequestExists(senderId, receiverId);

        if(!connectionRequestExists){
            throw new RuntimeException("Connection request does not exist");
        }

        log.info("Rejecting connection from user with id: {} to user with id: {}", senderId, receiverId);
        personRepository.rejectConnectionRequest(senderId, receiverId);
        return true;
    }

    public Boolean CheckUserExists(Long userId) {
        return usersClient.checkUserExists(userId);
    }

    public void addPerson(Long userId) {

        Boolean check=personRepository.checkUserExistsInGraph(userId);
        if(check) return;

        UserDto userDto = usersClient.getUserById(userId);

        personRepository.addPerson(userDto.getName(), userId);
    }
}
