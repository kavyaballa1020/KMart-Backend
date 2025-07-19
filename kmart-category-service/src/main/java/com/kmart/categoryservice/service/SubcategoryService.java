package com.kmart.categoryservice.service;

import com.kmart.categoryservice.client.AuthClient;
import com.kmart.categoryservice.model.Category;
import com.kmart.categoryservice.model.Subcategory;
import com.kmart.categoryservice.repository.CategoryRepository;
import com.kmart.categoryservice.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthClient authClient;

    // Fetch all subcategories
    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }

    // Get subcategories by category ID
    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        return subcategoryRepository.findByCategory(category);
    }

    // Create a subcategory under a category (Admin only)
    public Subcategory createSubcategory(Long categoryId, Subcategory subcategory, String token) {
        validateAdmin(token);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        if (subcategoryRepository.existsByNameAndCategory(subcategory.getName(), category)) {
            throw new RuntimeException("Subcategory already exists in this category");
        }

        subcategory.setCategory(category);
        return subcategoryRepository.save(subcategory);
    }

    // Get subcategory by ID
    public Subcategory getSubcategoryById(Long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subcategory not found with ID: " + id));
    }

    // Update subcategory (Admin only)
    public Subcategory updateSubcategory(Long id, Subcategory updatedSubcategory, String token) {
        validateAdmin(token);

        Subcategory subcategory = getSubcategoryById(id);
        subcategory.setName(updatedSubcategory.getName());

        return subcategoryRepository.save(subcategory);
    }

    // Delete subcategory (Admin only)
    public void deleteSubcategory(Long id, String token) {
        validateAdmin(token);

        Subcategory subcategory = getSubcategoryById(id);
        subcategoryRepository.delete(subcategory);
    }

    // ✅ Fixed: Check if token belongs to an admin
    private void validateAdmin(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Invalid or missing token");
        }

        String role = authClient.getUserRole("Bearer " + token);

        if (!"ROLE_ADMIN".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Access denied: Only admin can perform this action.");
        }
    }
}
