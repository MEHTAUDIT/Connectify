package com.sdp.post_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PostCreatedEvent {

    Long creatorId;
    String content;
    Long postId;
}
