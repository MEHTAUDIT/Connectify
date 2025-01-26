package com.sdp.notification_service.consumer;

import com.sdp.connections_service.event.AcceptConnectionRequestEvent;
import com.sdp.connections_service.event.SendConnectionRequestEvent;
import com.sdp.notification_service.clients.UsersClient;
import com.sdp.notification_service.dto.UserDto;
import com.sdp.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsServiceConsumer {

    private final SendNotification sendNotification;
    private final UsersClient usersClient;
    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        log.info("Received SendConnectionRequestEvent: {}", sendConnectionRequestEvent);

        UserDto user = usersClient.getUserById(sendConnectionRequestEvent.getSenderId());
        String message = String.format("You have received a connection request from %s", user.getName());

        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);

    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        log.info("Received AcceptConnectionRequestEvent: {}", acceptConnectionRequestEvent);

        UserDto user = usersClient.getUserById(acceptConnectionRequestEvent.getReceiverId());
        String message = String.format("Your connection request to %s has been accepted", user.getName());

        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
    }

}
