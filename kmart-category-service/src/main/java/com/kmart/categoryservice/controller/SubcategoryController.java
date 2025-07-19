package com.kmart.categoryservice.controller;

import com.kmart.categoryservice.model.Subcategory;
import com.kmart.categoryservice.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping
    public ResponseEntity<List<Subcategory>> getAllSubcategories() {
        return ResponseEntity.ok(subcategoryService.getAllSubcategories());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Subcategory>> getSubcategoriesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(subcategoryService.getSubcategoriesByCategoryId(categoryId));
    }

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<Subcategory> createSubcategory(
            @RequestHeader("Authorization") String token,
            @PathVariable Long categoryId,
            @RequestBody Subcategory subcategory) {
        return ResponseEntity.ok(subcategoryService.createSubcategory(categoryId, subcategory, cleanToken(token)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subcategory> getSubcategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(subcategoryService.getSubcategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subcategory> updateSubcategory(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Subcategory updatedSubcategory) {
        return ResponseEntity.ok(subcategoryService.updateSubcategory(id, updatedSubcategory, cleanToken(token)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubcategory(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        subcategoryService.deleteSubcategory(id, cleanToken(token));
        return ResponseEntity.ok("Subcategory deleted successfully");
    }

    // Utility method to strip "Bearer " if present
    private String cleanToken(String token) {
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}
