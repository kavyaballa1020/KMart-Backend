package com.kmart.productservice.feign;

import com.kmart.productservice.payload.SubCategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "category-service")
public interface CategoryClient {

    @GetMapping("/api/subcategories/{id}")
    SubCategoryDto getSubCategoryById(
        @PathVariable("id") Long id,
        @RequestHeader("Authorization") String token
    );
}
