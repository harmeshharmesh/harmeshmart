package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for User entities.
 */
public interface UserDAO {

    /**
     * Find a user by their unique primary key ID.
     *
     * @param id User primary key ID
     * @return Optional containing the User if found
     * @throws SQLException On database communication failure
     */
    Optional<User> findById(Long id) throws SQLException;

    /**
     * Find a user by their unique email address.
     *
     * @param email User email
     * @return Optional containing the User if found
     * @throws SQLException On database communication failure
     */
    Optional<User> findByEmail(String email) throws SQLException;

    /**
     * Insert a new user record.
     *
     * @param user User to persist
     * @return The persisted User with generated ID
     * @throws SQLException On database communication failure
     */
    User save(User user) throws SQLException;

    /**
     * Update user details.
     *
     * @param user User with updated attributes
     * @throws SQLException On database communication failure
     */
    void update(User user) throws SQLException;

    /**
     * Retrieve all users in the system.
     *
     * @return List of all users
     * @throws SQLException On database communication failure
     */
    List<User> findAll() throws SQLException;

    /**
     * Count total users on the platform.
     *
     * @return Total user count
     * @throws SQLException On database communication failure
     */
    int countTotalUsers() throws SQLException;
}
