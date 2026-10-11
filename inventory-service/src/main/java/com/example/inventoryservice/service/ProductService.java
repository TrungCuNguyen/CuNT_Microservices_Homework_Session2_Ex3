package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.ComparisonProofDTO;
import com.example.inventoryservice.dto.DatabaseConnectionInfoDTO;
import com.example.inventoryservice.dto.ProductRequestDTO;
import com.example.inventoryservice.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO);
    void deleteProduct(Long id);
    DatabaseConnectionInfoDTO getDatabaseConnectionInfo();
    ComparisonProofDTO getCrossServiceComparison();
}
