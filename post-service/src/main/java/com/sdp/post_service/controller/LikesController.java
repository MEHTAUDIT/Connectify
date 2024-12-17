package com.sdp.post_service.controller;

import com.sdp.post_service.entity.PostLike;
import com.sdp.post_service.service.PostLikeService;
import com.sdp.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor

public class LikesController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {

        postLikeService.likePost(postId,1L);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {

        postLikeService.unlikePost(postId,1L);
        return ResponseEntity.noContent().build();
    }

}
