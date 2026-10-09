package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.dao.CategoryDAO;
import com.harmesh.harmeshmart.dao.DAOFactory;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.model.Category;
import com.harmesh.harmeshmart.service.CategoryService;
import com.harmesh.harmeshmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of CategoryService.
 */
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryDAO categoryDAO;

    public CategoryServiceImpl() {
        this(DAOFactory.getCategoryDAO());
    }

    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Category> getAllCategories() throws AppException {
        try {
            return categoryDAO.findAll();
        } catch (SQLException e) {
            logger.error("Error retrieving categories: {}", e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve categories.");
        }
    }

    @Override
    public Optional<Category> getCategoryById(Long id) throws AppException {
        try {
            return categoryDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Error retrieving category ID {}: {}", id, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve category.");
        }
    }

    @Override
    public Optional<Category> getCategoryBySlug(String slug) throws AppException {
        try {
            return categoryDAO.findBySlug(slug);
        } catch (SQLException e) {
            logger.error("Error retrieving category slug {}: {}", slug, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to retrieve category.");
        }
    }

    @Override
    public Category createCategory(Category category) throws AppException {
        ValidationUtil.validateRequired(category.getName(), "name");
        if (category.getSlug() == null || category.getSlug().trim().isEmpty()) {
            category.setSlug(category.getName().trim().toLowerCase().replaceAll("[^a-z0-9]+", "-"));
        }
        try {
            return categoryDAO.save(category);
        } catch (SQLException e) {
            logger.error("Error creating category {}: {}", category.getName(), e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to create category.");
        }
    }

    @Override
    public void updateCategory(Category category) throws AppException {
        ValidationUtil.validateRequired(category.getName(), "name");
        try {
            categoryDAO.update(category);
        } catch (SQLException e) {
            logger.error("Error updating category ID {}: {}", category.getId(), e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to update category.");
        }
    }

    @Override
    public void deleteCategory(Long id) throws AppException {
        try {
            categoryDAO.delete(id);
        } catch (SQLException e) {
            logger.error("Error deleting category ID {}: {}", id, e.getMessage(), e);
            throw new AppException("DATABASE_ERROR", "Failed to delete category.");
        }
    }
}
