package com.sdp.post_service.service;

import com.sdp.post_service.entity.Post;
import com.sdp.post_service.entity.PostLike;
import com.sdp.post_service.exception.BadRequestException;
import com.sdp.post_service.exception.ResourceNotFoundException;
import com.sdp.post_service.repository.PostLikeRepository;
import com.sdp.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public void likePost(Long postId, Long userId) {

        log.info("Adding like to post with id: {} by user with id: {}", postId, userId);
        System.out.println("Adding like to post with id: {} by user with id: {}" + postId + userId);
        boolean exists = postRepository.existsById(postId);

        System.out.println("exists: " + exists);

        if(!exists) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);

        if(isLiked) {
            throw new BadRequestException("Post already liked by user with id: " + userId);
        }

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);

        postLikeRepository.save(postLike);

        log.info("Like added to post with id: {} by user with id: {}", postId, userId);

    }

    public void unlikePost(Long postId, Long userId) {

        log.info("Removing like to post with id: {} by user with id: {}", postId, userId);
        System.out.println("Removing like to post with id: {} by user with id: {}" + postId + userId);
        boolean exists = postRepository.existsById(postId);

        System.out.println("exists: " + exists);

        if(!exists) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        boolean isLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);

        if(!isLiked) {
            throw new BadRequestException("Post not liked by user with id: " + userId);
        }

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);

        log.info("Like removed to post with id: {} by user with id: {}", postId, userId);

    }
}
