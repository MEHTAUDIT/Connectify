package com.sdp.post_service.controller;

import com.sdp.post_service.auth.UserContextHolder;
import com.sdp.post_service.dto.PostCreateRequestDto;
import com.sdp.post_service.dto.PostDto;
import com.sdp.post_service.entity.Post;
import com.sdp.post_service.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor

public class PostsController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto) {

        PostDto createdPost = postService.createPost(postCreateRequestDto);

        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {

        PostDto postDto = postService.getPostById(postId);

        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {

        List<PostDto> postDtos = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(postDtos);
    }

}
