package com.kmart.productservice.service;

import com.kmart.productservice.model.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getProductsBySubcategoryId(Long subcategoryId);
}
