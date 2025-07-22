package com.kmart.productservice.controller;

import com.kmart.productservice.model.Product;
import com.kmart.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    
    public ProductController(ProductService service) {
        this.service = service;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product addProduct(@RequestPart("product") Product product,
                              @RequestPart(value = "image", required = false) MultipartFile image,
                              @RequestHeader("Authorization") String token) throws IOException {
        return service.addProduct(product, image, token);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Product updateProduct(@PathVariable Long id,
                                 @RequestPart("product") Product product,
                                 @RequestPart(value = "image", required = false) MultipartFile image,
                                 @RequestHeader("Authorization") String token) throws IOException {
        return service.updateProduct(id, product, image, token);
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

    // Optional: to retrieve image directly
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        return service.getProductImage(id);
    }
}
