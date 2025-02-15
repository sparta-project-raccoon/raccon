package com.sparta.spartaproject.domain.category;

import com.sparta.spartaproject.dto.request.CreateCategoryRequestDto;
import com.sparta.spartaproject.dto.request.UpdateCategoryRequestDto;
import com.sparta.spartaproject.dto.response.CategoryDto;
import com.sparta.spartaproject.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(
            categoryMapper::toCategoryDto
        ).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        return categoryMapper.toCategoryDto(
            getCategoryById(id)
        );
    }

    @Transactional
    public void createCategory(CreateCategoryRequestDto request) {
        if (existsCategoryByName(request.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다.");
        }

        Category newCategory = categoryMapper.toCategory(request);
        categoryRepository.save(newCategory);

        log.info("카테고리 생성: {}", request.name());
    }

    @Transactional
    public void updateCategory(Long id, UpdateCategoryRequestDto update) {
        if (existsCategoryByName(update.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 카테고리입니다.");
        }

        Category category = getCategoryById(id);
        category.update(update);
    }

    @Transactional
    public void deleteCategory(Long id) {
        // TODO: 사용중인 카테고리인지 검사 - 사용중이면 삭제 X

        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }


    public Category getCategoryById(Long id) {
        // TODO: 카테고리 에러 메시지 적용, 404
        return categoryRepository.findById(id)
            .orElseThrow();
    }

    public Boolean existsCategoryByName(String name) {
        return categoryRepository.existsByName(name);
    }
}