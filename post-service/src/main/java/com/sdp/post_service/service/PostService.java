package com.sdp.post_service.service;

import com.sdp.post_service.auth.UserContextHolder;
import com.sdp.post_service.clients.ConnectionsClient;
import com.sdp.post_service.dto.PersonDto;
import com.sdp.post_service.dto.PostCreateRequestDto;
import com.sdp.post_service.dto.PostDto;
import com.sdp.post_service.entity.Post;
import com.sdp.post_service.event.PostCreatedEvent;
import com.sdp.post_service.exception.ResourceNotFoundException;
import com.sdp.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionsClient connectionsClient;
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {

        Long userId= UserContextHolder.getCurrentUserId();

        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .creatorId(userId)
                .content(post.getContent())
                .postId(post.getId())
                .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);

        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId){

        Post post=postRepository.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post not Found with id :"+postId));

        return modelMapper.map(post,PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts
                .stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

}
