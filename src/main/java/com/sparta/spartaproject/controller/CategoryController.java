package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.category.CategoryService;
import com.sparta.spartaproject.dto.request.CreateCategoryRequestDto;
import com.sparta.spartaproject.dto.request.UpdateCategoryRequestDto;
import com.sparta.spartaproject.dto.response.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Description(
        "카테고리 전체 조회"
    )
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @Description(
        "카테고리 상세 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @Description(
        "카테고리 생성"
    )
    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CreateCategoryRequestDto request) {
        categoryService.createCategory(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "카테고리 수정"
    )
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable UUID id, @RequestBody UpdateCategoryRequestDto update) {
        categoryService.updateCategory(id, update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "카테고리 삭제"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}