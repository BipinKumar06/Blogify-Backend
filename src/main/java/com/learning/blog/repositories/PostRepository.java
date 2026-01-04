package com.learning.blog.repositories;

import com.learning.blog.domain.PostStatus;
import com.learning.blog.domain.entities.Category;
import com.learning.blog.domain.entities.Post;
import com.learning.blog.domain.entities.Tag;
import com.learning.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findALlByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);
    List<Post> findALlByStatusAndCategory(PostStatus status, Category category);
    List<Post> findALlByStatusAndTagsContaining(PostStatus status, Tag tag);
    List<Post> findALlByStatus(PostStatus status);
    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);
}
