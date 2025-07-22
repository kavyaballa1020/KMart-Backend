package com.kmart.productservice.service;

import com.kmart.productservice.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    Product addProduct(Product product, MultipartFile image, String token) throws IOException;
    Product updateProduct(Long id, Product product, MultipartFile image, String token) throws IOException;
    void deleteProduct(Long id, String token);
    Product getProduct(Long id);
    List<Product> getAll();
    List<Product> getBySubCategory(Long subCategoryId);
    ResponseEntity<byte[]> getProductImage(Long id);
}
