package com.kmart.productservice.service;

import com.kmart.productservice.feign.AuthClient;
import com.kmart.productservice.feign.CategoryClient;
import com.kmart.productservice.model.Product;
import com.kmart.productservice.payload.SubCategoryDto;
import com.kmart.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final AuthClient authClient;
    private final CategoryClient categoryClient;

    @Autowired
    public ProductServiceImpl(ProductRepository repo, AuthClient authClient, CategoryClient categoryClient) {
        this.repo = repo;
        this.authClient = authClient;
        this.categoryClient = categoryClient;
    }
    private void validateAdminOrVendor(String token) {
        String cleanedToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String role = authClient.getRoleFromToken(cleanedToken);
        if (!(role.equals("ROLE_ADMIN") || role.equals("ROLE_VENDOR"))) {
            throw new RuntimeException("Access Denied");
        }
    }


    @Override
    public Product addProduct(Product product, String token) {
        validateAdminOrVendor(token);

        SubCategoryDto subCat = categoryClient.getSubCategoryById(product.getSubCategoryId(), token);
        if (subCat == null) {
            throw new RuntimeException("Invalid subcategory");
        }

        return repo.save(product);
    }


    @Override
    public Product updateProduct(Long id, Product product, String token) {
        validateAdminOrVendor(token);
        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setImageUrl(product.getImageUrl());
        return repo.save(existing);
    }
    

    @Override
    public void deleteProduct(Long id, String token) {
        validateAdminOrVendor(token);
        repo.deleteById(id);
    }

    @Override
    public Product getProduct(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    @Override
    public List<Product> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Product> getBySubCategory(Long subCategoryId) {
        return repo.findBySubCategoryId(subCategoryId);
    }
}
