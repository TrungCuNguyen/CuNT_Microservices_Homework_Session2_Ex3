# Hệ Thống Microservices: User-Service & Inventory-Service

Dự án triển khai kiến trúc Microservices độc lập theo đúng yêu cầu đề bài:
- **Tách biệt cơ sở dữ liệu**: Mỗi service sở hữu một Database vật lý riêng biệt trên PostgreSQL (`user_db` và `inventory_db`).
- **Khả năng chịu lỗi (Fault Isolation)**: Nếu một service hoặc DB của một bên bảo trì/gặp sự cố, service bên kia vẫn hoạt động bình thường 100%.
- **Chứng minh độc lập kết nối**: Cung cấp API trực tiếp truy vấn metadata của kết nối PostgreSQL (`current_database()`, `pg_backend_pid()`, HikariCP pool) để chứng minh hai bên không hề dùng chung kết nối hay cơ sở dữ liệu.

---

## 1. Công Nghệ Sử Dụng

- **Spring Boot**: 4.1.1
- **JDK**: 17
- **Build Tool**: Gradle (Gradle 9.7.1)
- **Configuration**: `application.properties`
- **Database**: PostgreSQL 18
- **ORM / Persistence**: Spring Data JPA / Hibernate
- **REST Client**: Spring 6 `RestClient` (cho giao tiếp liên service)
- **Tiện ích**: Lombok, Jakarta Validation

---

## 2. Cấu Trúc Dự Án

Cả 2 dịch vụ đều được phân tầng chuẩn mực theo yêu cầu:

```
ex3_ss2_micro/
├── database.sql                                      # Script tạo 2 database, bảng và dữ liệu mẫu
├── build.gradle                                      # Cấu hình Gradle chung
├── settings.gradle                                   # Khai báo multi-project
├── gradlew / gradlew.bat                             # Gradle Wrapper
│
├── user-service/                                     # Dịch vụ User (Port 8081 -> user_db)
│   ├── build.gradle
│   ├── settings.gradle
│   └── src/
│       ├── main/
│       │   ├── java/com/example/userservice/
│       │   │   ├── UserServiceApplication.java
│       │   │   ├── controller/
│       │   │   │   └── UserController.java
│       │   │   ├── service/
│       │   │   │   ├── UserService.java
│       │   │   │   └── impl/UserServiceImpl.java
│       │   │   ├── repository/
│       │   │   │   └── UserRepository.java
│       │   │   ├── entity/
│       │   │   │   └── User.java
│       │   │   ├── dto/
│       │   │   │   ├── UserRequestDTO.java
│       │   │   │   ├── UserResponseDTO.java
│       │   │   │   ├── DatabaseConnectionInfoDTO.java
│       │   │   │   └── ApiResponse.java
│       │   │   └── exception/
│       │   │       ├── ResourceNotFoundException.java
│       │   │       ├── DuplicateResourceException.java
│       │   │       ├── ErrorResponse.java
│       │   │       └── GlobalExceptionHandler.java
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│
└── inventory-service/                                # Dịch vụ Inventory (Port 8082 -> inventory_db)
    ├── build.gradle
    ├── settings.gradle
    └── src/
        ├── main/
        │   ├── java/com/example/inventoryservice/
        │   │   ├── InventoryServiceApplication.java
        │   │   ├── controller/
        │   │   │   └── ProductController.java
        │   │   ├── service/
        │   │   │   ├── ProductService.java
        │   │   │   └── impl/ProductServiceImpl.java
        │   │   ├── repository/
        │   │   │   └── ProductRepository.java
        │   │   ├── entity/
        │   │   │   └── Product.java
        │   │   ├── dto/
        │   │   │   ├── ProductRequestDTO.java
        │   │   │   ├── ProductResponseDTO.java
        │   │   │   ├── UserSummaryDTO.java
        │   │   │   ├── ComparisonProofDTO.java
        │   │   │   ├── DatabaseConnectionInfoDTO.java
        │   │   │   └── ApiResponse.java
        │   │   └── exception/
        │   │       ├── ResourceNotFoundException.java
        │   │       ├── DuplicateResourceException.java
        │   │       ├── ErrorResponse.java
        │   │       └── GlobalExceptionHandler.java
        │   └── resources/
        │       └── application.properties
        └── test/
```

---

## 3. Cơ Sở Dữ Liệu PostgreSQL

File khởi tạo: [`database.sql`](file:///e:/Dev/JAVA/ex3_ss2_micro/database.sql)

1. **`user_db`**:
   - Bảng: `users`
   - Cột: `id`, `username`, `email`, `full_name`, `status`, `created_at`
2. **`inventory_db`**:
   - Bảng: `products`
   - Cột: `id`, `product_name`, `sku`, `price`, `quantity`, `description`, `created_at`

---

## 4. Danh Sách API Endpoints

### 4.1. User-Service (`http://localhost:8081`)

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/users` | Lấy danh sách toàn bộ users |
| `GET` | `/api/users/{id}` | Lấy chi tiết user theo ID |
| `POST` | `/api/users` | Tạo mới user |
| `PUT` | `/api/users/{id}` | Cập nhật thông tin user |
| `DELETE` | `/api/users/{id}` | Xóa user |
| `GET` | `/api/users/db-connection` | **Chứng minh kết nối DB**: trả về tên DB `user_db`, backend PID, thông tin session |

### 4.2. Inventory-Service (`http://localhost:8082`)

| Method | Endpoint | Mô tả |
|---|---|---|
| `GET` | `/api/products` | Lấy danh sách toàn bộ sản phẩm |
| `GET` | `/api/products/{id}` | Lấy chi tiết sản phẩm theo ID |
| `POST` | `/api/products` | Thêm mới sản phẩm |
| `PUT` | `/api/products/{id}` | Cập nhật sản phẩm |
| `DELETE` | `/api/products/{id}` | Xóa sản phẩm |
| `GET` | `/api/products/db-connection` | **Chứng minh kết nối DB**: trả về tên DB `inventory_db`, backend PID |
| `GET` | `/api/products/cross-service-comparison` | **Yêu cầu cốt lõi của đề bài**: Lấy dữ liệu đồng thời từ cả 2 service và trả về bằng chứng so sánh không dùng chung kết nối |

---

## 5. Hướng Dẫn Chạy & Kiểm Thử

### Bước 1: Khởi tạo dữ liệu PostgreSQL (đã chạy sẵn)
```powershell
psql -U postgres -p 5432 -f database.sql
```

### Bước 2: Chạy đồng thời 2 Service
Mở 2 cửa sổ terminal:
- Terminal 1 (User-Service):
  ```powershell
  .\gradlew.bat :user-service:bootRun
  ```
- Terminal 2 (Inventory-Service):
  ```powershell
  .\gradlew.bat :inventory-service:bootRun
  ```

### Bước 3: Kiểm tra API Chứng Minh Tách Biệt Kết Nối
Gọi API:
```powershell
curl http://localhost:8082/api/products/cross-service-comparison
```
Kết quả trả về sẽ hiển thị rõ ràng:
- `inventoryServiceDbInfo.databaseName`: `inventory_db` (PID: X)
- `userServiceDbInfo.databaseName`: `user_db` (PID: Y)
- `completelyIsolated`: `true`

### Bước 4: Kiểm tra tính năng chịu lỗi (Fault Isolation)
1. Dừng `User-Service` (giả lập bảo trì `user_db`).
2. Gọi lại API:
   ```powershell
   curl http://localhost:8082/api/products
   curl http://localhost:8082/api/products/cross-service-comparison
   ```
3. Kết quả: `Inventory-Service` vẫn phục vụ dữ liệu sản phẩm bình thường 100%, ghi nhận trạng thái `User-Service` đang bảo trì mà không bị lỗi lan truyền hệ thống!
