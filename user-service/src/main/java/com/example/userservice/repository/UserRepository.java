package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT current_database()", nativeQuery = true)
    String getCurrentDatabaseName();

    @Query(value = "SELECT pg_backend_pid()", nativeQuery = true)
    Integer getBackendPid();
}
