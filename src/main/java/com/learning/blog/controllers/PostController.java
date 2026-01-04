package com.learning.blog.controllers;

import com.learning.blog.domain.CreatePostRequest;
import com.learning.blog.domain.UpdatePostRequest;
import com.learning.blog.domain.dtos.CreatePostRequestDto;
import com.learning.blog.domain.dtos.PostDto;
import com.learning.blog.domain.dtos.UpdatePostRequestDto;
import com.learning.blog.domain.entities.Post;
import com.learning.blog.domain.entities.User;
import com.learning.blog.mappers.PostMapper;
import com.learning.blog.services.PostService;
import com.learning.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId){
        List<Post> posts = postService.getALlPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId){
        User loggerInUser = userService.getUserById(userId);
        List<Post> posts = postService.getDraftPosts(loggerInUser);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostRequestDto createPostRequestDto,
                                              @RequestAttribute UUID userId){
        User loggerInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);

        Post createdPost = postService.createPost(loggerInUser, createPostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id, @RequestBody UpdatePostRequestDto updatePostRequestDto
            ){
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto updatedPostDto = postMapper.toDto(updatedPost);
        return ResponseEntity.ok(updatedPostDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable UUID id
    ){
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toDto(post);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID id
    ){
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
