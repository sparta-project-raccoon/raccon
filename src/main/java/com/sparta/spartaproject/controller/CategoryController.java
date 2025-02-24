package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.category.CategoryService;
import com.sparta.spartaproject.dto.request.CreateCategoryRequestDto;
import com.sparta.spartaproject.dto.request.UpdateCategoryRequestDto;
import com.sparta.spartaproject.dto.response.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @Description(
        "카테고리 상세 조회"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @Description(
        "카테고리 생성"
    )
    @PostMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> createCategory(@RequestBody CreateCategoryRequestDto request) {
        categoryService.createCategory(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "카테고리 수정"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> updateCategory(@PathVariable("id") UUID id, @RequestBody UpdateCategoryRequestDto update) {
        categoryService.updateCategory(id, update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "카테고리 삭제"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}