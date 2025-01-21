package com.sdp.connections_service.service;

import com.sdp.connections_service.auth.UserContextHolder;
import com.sdp.connections_service.entity.Person;
import com.sdp.connections_service.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j

public class ConnectionsService {

    private final PersonRepository personRepository;

    public List<Person> getFirstDegreeConnections(@RequestHeader("X-User-Id") Long userId) {

//        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting first degree connections for user with id: {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }
}
