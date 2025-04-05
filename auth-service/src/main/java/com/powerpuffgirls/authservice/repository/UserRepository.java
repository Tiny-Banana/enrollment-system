package com.powerpuffgirls.authservice.repository;

import com.powerpuffgirls.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // JpaRepository provides CRUD operations automatically
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}