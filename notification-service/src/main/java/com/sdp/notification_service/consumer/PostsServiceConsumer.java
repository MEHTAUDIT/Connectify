package com.sdp.notification_service.consumer;

import com.sdp.notification_service.clients.UsersClient;
import com.sdp.notification_service.dto.PersonDto;
import com.sdp.notification_service.clients.ConnectionsClient;
import com.sdp.notification_service.dto.UserDto;
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
    private final UsersClient usersClient;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreatedEvent(PostCreatedEvent postCreatedEvent) {
        log.info("Received PostCreatedEvent: {}", postCreatedEvent);

        List<PersonDto> firstDegreeConnections = connectionsClient.getFirstDegreeConnections(postCreatedEvent.getCreatorId());

        UserDto user = usersClient.getUserById(postCreatedEvent.getCreatorId());

        log.info("User: {}", user);

        for(PersonDto personDto : firstDegreeConnections){
            log.info("Notifying user with id: {} about the post with id: {}", personDto.getId(), postCreatedEvent.getPostId());
            String message = String.format("New post created by your connection %s", user.getName());
            sendNotification.send(personDto.getUserId(), message);
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLikedEvent(PostLikedEvent postLikedEvent){

        log.info("Received PostLikedEvent: {}", postLikedEvent);

        UserDto user = usersClient.getUserById(postLikedEvent.getLikedByUserId());

        String message = String.format("Your post has been liked by %s", user.getName());
        sendNotification.send(postLikedEvent.getCreatorId(), message);

//        log.info("Received PostLikedEvent");
//        String message=String.format("Your Post %d has been liked by %d",postLikedEvent.getPostId(),postLikedEvent.getLikedByUserId());
//        sendNotification.send(postLikedEvent.getCreatorId(),message);
    }


}
