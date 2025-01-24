package com.sdp.notification_service.consumer;

import com.sdp.notification_service.dto.PersonDto;
import com.sdp.notification_service.clients.ConnectionsClient;
import com.sdp.post_service.event.PostCreatedEvent;
import com.sdp.post_service.event.PostLikedEvent;
import com.sdp.notification_service.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        log.info("Received PostCreatedEvent: {}", postCreatedEvent);

        List<PersonDto> firstDegreeConnections = connectionsClient.getFirstDegreeConnections(postCreatedEvent.getCreatorId());

        for(PersonDto personDto : firstDegreeConnections){
            log.info("Notifying user with id: {} about the post with id: {}", personDto.getId(), postCreatedEvent.getPostId());

            sendNotification.send(personDto.getId(),"New post created by your connection");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLikedEvent(PostLikedEvent postLikedEvent){

        log.info("Received PostLikedEvent");
        String message=String.format("Your Post %d has been liked by %d",postLikedEvent.getPostId(),postLikedEvent.getLikedByUserId());
        sendNotification.send(postLikedEvent.getCreatorId(),message);
    }


}
