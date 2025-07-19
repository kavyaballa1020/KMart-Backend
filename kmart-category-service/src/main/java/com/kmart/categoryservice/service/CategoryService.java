package com.kmart.categoryservice.service;

import com.kmart.categoryservice.client.AuthClient;
import com.kmart.categoryservice.model.Category;
import com.kmart.categoryservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthClient authClient;

    // Fetch all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Create a new category (Admin only)
    public Category createCategory(Category category, String token) {
        validateAdmin(token);

        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category already exists");
        }

        return categoryRepository.save(category);
    }

    // Get category by ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
    }

    // Update category by ID (Admin only)
    public Category updateCategory(Long id, Category updatedCategory, String token) {
        validateAdmin(token);

        Category category = getCategoryById(id);
        category.setName(updatedCategory.getName());

        return categoryRepository.save(category);
    }

    // Delete category by ID (Admin only)
    public void deleteCategory(Long id, String token) {
        validateAdmin(token);

        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    // ✅ Fixed: Check if token belongs to an admin
    public void validateAdmin(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Invalid or missing token");
        }

        // 👇 Correct: add Bearer here when calling auth service
        String role = authClient.getUserRole("Bearer " + token);

        if (!"ROLE_ADMIN".equals(role)) {
            throw new RuntimeException("Access denied. Admin role required");
        }
    }

}
