package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Service for category management.
 */
public interface CategoryService {

    List<Category> getAllCategories() throws AppException;

    Optional<Category> getCategoryById(Long id) throws AppException;

    Optional<Category> getCategoryBySlug(String slug) throws AppException;

    Category createCategory(Category category) throws AppException;

    void updateCategory(Category category) throws AppException;

    void deleteCategory(Long id) throws AppException;
}
