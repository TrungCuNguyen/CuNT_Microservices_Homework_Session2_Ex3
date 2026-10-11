package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.*;
import com.example.inventoryservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách sản phẩm thành công", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.ok("Lấy thông tin sản phẩm thành công", product));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO created = productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo mới sản phẩm thành công", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO updated = productService.updateProduct(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật sản phẩm thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa sản phẩm thành công", null));
    }

    /**
     * API chứng minh kết nối cơ sở dữ liệu riêng biệt của Inventory-Service
     */
    @GetMapping("/db-connection")
    public ResponseEntity<ApiResponse<DatabaseConnectionInfoDTO>> getDatabaseConnection() {
        DatabaseConnectionInfoDTO info = productService.getDatabaseConnectionInfo();
        return ResponseEntity.ok(ApiResponse.ok("Thông tin kết nối Database của Inventory-Service", info));
    }

    /**
     * API lấy dữ liệu từ cả 2 service và chứng minh rằng chúng không hề dùng chung kết nối
     */
    @GetMapping("/cross-service-comparison")
    public ResponseEntity<ApiResponse<ComparisonProofDTO>> getCrossServiceComparison() {
        ComparisonProofDTO comparison = productService.getCrossServiceComparison();
        return ResponseEntity.ok(ApiResponse.ok("Dữ liệu và bằng chứng tách biệt kết nối của cả 2 services", comparison));
    }
}
