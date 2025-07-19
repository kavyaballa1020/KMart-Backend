package com.kmart.categoryservice.controller;

import com.kmart.categoryservice.model.Category;
import com.kmart.categoryservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestHeader("Authorization") String token,
            @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category, cleanToken(token)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Category updatedCategory) {
        return ResponseEntity.ok(categoryService.updateCategory(id, updatedCategory, cleanToken(token)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        categoryService.deleteCategory(id, cleanToken(token));
        return ResponseEntity.ok("Category deleted successfully");
    }

    // Utility method to strip "Bearer " if present
    private String cleanToken(String token) {
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}	
