package com.sparta.spartaproject.domain.category;

import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.dto.request.CreateCategoryRequestDto;
import com.sparta.spartaproject.dto.request.UpdateCategoryRequestDto;
import com.sparta.spartaproject.dto.response.CategoryDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;
    private final CircularService circularService;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(
            categoryMapper::toCategoryDto
        ).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategory(UUID id) {
        return categoryMapper.toCategoryDto(
            getCategoryById(id)
        );
    }

    @Transactional
    public void createCategory(CreateCategoryRequestDto request) {
        if (existsCategoryByName(request.name())) {
            throw new BusinessException(ErrorCode.ALREADY_CATEGORY);
        }

        Category newCategory = categoryMapper.toCategory(request);
        categoryRepository.save(newCategory);

        log.info("카테고리 생성: {}", request.name());
    }

    @Transactional
    public void updateCategory(UUID id, UpdateCategoryRequestDto update) {
        if (existsCategoryByName(update.name())) {
            throw new BusinessException(ErrorCode.ALREADY_CATEGORY);
        }

        Category category = getCategoryById(id);
        category.update(update);

        log.info("카테고리: {}, 수정: {}", id, update.name());
    }

    @Transactional
    public void deleteCategory(UUID id) {
        Category category = getCategoryById(id);

        if (circularService.getStoreCategoryService().existsByCategory(category)) {
            throw new BusinessException(ErrorCode.CATEGORY_IS_USED);
        }

        categoryRepository.delete(category);
        log.info("카테고리: {}, 삭제", id);
    }


    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public Boolean existsCategoryByName(String name) {
        return categoryRepository.existsByName(name);
    }
}