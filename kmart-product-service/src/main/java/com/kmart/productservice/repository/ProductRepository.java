package com.kmart.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmart.productservice.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySubCategoryId(Long subCategoryId);
}
