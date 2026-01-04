package com.learning.blog.mappers;

import com.learning.blog.domain.CreatePostRequest;
import com.learning.blog.domain.UpdatePostRequest;
import com.learning.blog.domain.dtos.CreatePostRequestDto;
import com.learning.blog.domain.dtos.PostDto;
import com.learning.blog.domain.dtos.UpdatePostRequestDto;
import com.learning.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto createPostRequestDto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);
}
