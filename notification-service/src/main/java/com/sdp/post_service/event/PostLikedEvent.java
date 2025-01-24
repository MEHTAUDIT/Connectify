package com.sdp.post_service.event;

import lombok.Builder;
import lombok.Data;

@Data
public class PostLikedEvent {

    Long postId;
    Long CreatorId;
    Long likedByUserId;
}
