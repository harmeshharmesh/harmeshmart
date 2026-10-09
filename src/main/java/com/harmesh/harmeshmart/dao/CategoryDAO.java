package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Categories.
 */
public interface CategoryDAO {

    Optional<Category> findById(Long id) throws SQLException;

    Optional<Category> findBySlug(String slug) throws SQLException;

    List<Category> findAll() throws SQLException;

    Category save(Category category) throws SQLException;

    void update(Category category) throws SQLException;

    void delete(Long id) throws SQLException;
}
