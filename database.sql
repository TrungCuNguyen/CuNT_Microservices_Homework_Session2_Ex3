-- ===============================================================
-- Database Initialization Script for Microservices Architecture
-- 1. user_db (User-Service)
-- 2. inventory_db (Inventory-Service)
-- ===============================================================

-- Run this command if databases are not yet created:
-- CREATE DATABASE user_db;
-- CREATE DATABASE inventory_db;

-- ---------------------------------------------------------------
-- 1. Connect to user_db and create table 'users'
-- ---------------------------------------------------------------
\c user_db;

DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, email, full_name, status) VALUES
('nguyenvana', 'vana@example.com', 'Nguyen Van A', 'ACTIVE'),
('tranthib', 'thib@example.com', 'Tran Thi B', 'ACTIVE'),
('lethic', 'thic@example.com', 'Le Thi C', 'INACTIVE');

-- ---------------------------------------------------------------
-- 2. Connect to inventory_db and create table 'products'
-- ---------------------------------------------------------------
\c inventory_db;

DROP TABLE IF EXISTS products CASCADE;

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE,
    price NUMERIC(12, 2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO products (product_name, sku, price, quantity, description) VALUES
('Dell XPS 15 9530', 'LAPTOP-DELL-001', 1899.99, 15, 'Laptop Dell XPS 15 inch Intel Core i7 32GB RAM'),
('Apple iPhone 16 Pro Max', 'PHONE-APPL-002', 1299.00, 30, 'iPhone 16 Pro Max 256GB Desert Titanium'),
('Logitech MX Master 3S', 'MOUSE-LOGI-003', 99.50, 100, 'Ergonomic wireless mouse with 8K DPI sensor');
