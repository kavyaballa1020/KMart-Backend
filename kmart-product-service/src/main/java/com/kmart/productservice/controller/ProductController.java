package com.kmart.productservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.kmart.productservice.model.Product;
import com.kmart.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    
    public ProductController(ProductService service) {
        this.service = service;
    }


    @PostMapping
    public Product addProduct(@RequestBody Product product,
                              @RequestHeader("Authorization") String token) {
        return service.addProduct(product, token);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Product product,
                                 @RequestHeader("Authorization") String token) {
        return service.updateProduct(id, product, token);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id,
                              @RequestHeader("Authorization") String token) {
        service.deleteProduct(id, token);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return service.getProduct(id);
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/subcategory/{subCategoryId}")
    public List<Product> getBySubCategory(@PathVariable Long subCategoryId) {
        return service.getBySubCategory(subCategoryId);
    }
}
