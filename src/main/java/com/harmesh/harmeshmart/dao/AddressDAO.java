package com.harmesh.harmeshmart.dao;

import com.harmesh.harmeshmart.model.Address;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Address Book.
 */
public interface AddressDAO {

    Optional<Address> findById(Long id) throws SQLException;

    List<Address> findByUserId(Long userId) throws SQLException;

    Optional<Address> findDefaultByUserId(Long userId) throws SQLException;

    Address save(Address address) throws SQLException;

    void update(Address address) throws SQLException;

    void delete(Long id, Long userId) throws SQLException;

    void clearDefault(Long userId) throws SQLException;
}
