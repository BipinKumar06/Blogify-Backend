package com.learning.blog.services;

import com.learning.blog.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    public List<Category> listCategories();
    public Category createCategory(Category category);
    public void deleteCategory(UUID id);
    Category getCategoryById(UUID id);
}