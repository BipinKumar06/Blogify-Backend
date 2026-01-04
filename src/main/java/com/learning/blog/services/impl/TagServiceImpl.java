package com.learning.blog.services.impl;

import com.learning.blog.domain.entities.Tag;
import com.learning.blog.repositories.TagRepository;
import com.learning.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName).collect(Collectors.toSet());
        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder().name(name).posts(new HashSet<>()).build())
                .toList();
        List<Tag> savedTags = new ArrayList<>();
        if(!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }
        savedTags.addAll(existingTags);

        return savedTags;
    }

    @Override
    public void deleteTag(UUID id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if(tag.isPresent()){
            if(!tag.get().getPosts().isEmpty()){
                throw new IllegalStateException("Tag has post associated with it");
            }
            tagRepository.deleteById(id);
        }
    }

    @Override
    public Tag getTagById(UUID id){
        return tagRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tag is not find by id" + id));
    }

    @Override
    public List<Tag> getTagByIds(Set<UUID> ids) {
        List<Tag> foundTags = tagRepository.findAllById(ids);
        if(foundTags.size() != ids.size()){
            throw new EntityNotFoundException("Not all Specified tag ids exist");
        }
        return foundTags;
    }
}
