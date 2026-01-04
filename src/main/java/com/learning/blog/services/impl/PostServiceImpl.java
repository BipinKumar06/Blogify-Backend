package com.learning.blog.services.impl;

import com.learning.blog.domain.CreatePostRequest;
import com.learning.blog.domain.PostStatus;
import com.learning.blog.domain.UpdatePostRequest;
import com.learning.blog.domain.entities.Category;
import com.learning.blog.domain.entities.Post;
import com.learning.blog.domain.entities.Tag;
import com.learning.blog.domain.entities.User;
import com.learning.blog.repositories.PostRepository;
import com.learning.blog.services.CategoryService;
import com.learning.blog.services.PostService;
import com.learning.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final CategoryService categoryService;

    private static final int WORDS_PER_MINUTE = 200;

    @Transactional
    @Override
    public List<Post> getALlPosts(UUID categoryId, UUID tagId) {
        if(categoryId != null && tagId != null){
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findALlByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }
        if(categoryId != null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findALlByStatusAndCategory(PostStatus.PUBLISHED, category);
        }
        if(tagId != null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findALlByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }
        return postRepository.findALlByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        String postContent = createPostRequest.getContent();
        newPost.setContent(postContent);
        newPost.setAuthor(user);
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setReadingTime(calculateReadingTime(postContent));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        List<Tag> tags = tagService.getTagByIds(createPostRequest.getTagIds());
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id "+id));
        existingPost.setTitle(updatePostRequest.getTitle());
        String postContent = updatePostRequest.getContent();
        existingPost.setContent(postContent);
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(postContent));

        UUID categoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(categoryId)) {
            Category category = categoryService.getCategoryById(categoryId);
            existingPost.setCategory(category);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if(!existingTagIds.equals(updatePostRequestTagIds)){
            List<Tag> tags = tagService.getTagByIds(updatePostRequest.getTagIds());
            existingPost.setTags(new HashSet<>(tags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id "+postId));
    }

    @Override
    public void deletePost(UUID postId) {
        getPost(postId);
        postRepository.deleteById(postId);
    }

    private Integer calculateReadingTime(String content){
        if(content == null || content.isEmpty()){
            return 0;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount/WORDS_PER_MINUTE);
    }
}
