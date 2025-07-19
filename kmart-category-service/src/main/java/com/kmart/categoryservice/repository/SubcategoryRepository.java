package com.kmart.categoryservice.repository;

import com.kmart.categoryservice.model.Subcategory;
import com.kmart.categoryservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    List<Subcategory> findByCategory(Category category);
    boolean existsByNameAndCategory(String name, Category category);
}
