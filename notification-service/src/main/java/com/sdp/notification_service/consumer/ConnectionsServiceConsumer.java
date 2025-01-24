package com.sdp.notification_service.consumer;

import com.sdp.connections_service.event.AcceptConnectionRequestEvent;
import com.sdp.connections_service.event.SendConnectionRequestEvent;
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
    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        log.info("Received SendConnectionRequestEvent: {}", sendConnectionRequestEvent);

        String message = String.format("You have received a connection request from user %d", sendConnectionRequestEvent.getSenderId());

        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);

    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        log.info("Received AcceptConnectionRequestEvent: {}", acceptConnectionRequestEvent);

        String message = String.format("Your connection request to user %d has been accepted", acceptConnectionRequestEvent.getReceiverId());

        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
    }

}
