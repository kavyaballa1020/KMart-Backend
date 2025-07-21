package com.kmart.productservice.service;


import java.util.List;

import com.kmart.productservice.model.Product;

public interface ProductService {
    Product addProduct(Product product, String token);
    Product updateProduct(Long id, Product product, String token);
    void deleteProduct(Long id, String token);
    Product getProduct(Long id);
    List<Product> getAll();
    List<Product> getBySubCategory(Long subCategoryId);
}
