package com.learning.blog.services;

import com.learning.blog.domain.CreatePostRequest;
import com.learning.blog.domain.UpdatePostRequest;
import com.learning.blog.domain.entities.Post;
import com.learning.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getALlPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    Post getPost(UUID postId);
    void deletePost(UUID postId);
}
