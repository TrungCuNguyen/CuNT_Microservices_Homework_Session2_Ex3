package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);
    Optional<Product> findBySku(String sku);

    @Query(value = "SELECT current_database()", nativeQuery = true)
    String getCurrentDatabaseName();

    @Query(value = "SELECT pg_backend_pid()", nativeQuery = true)
    Integer getBackendPid();
}
