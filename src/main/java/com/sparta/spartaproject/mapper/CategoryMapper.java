package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.dto.request.CreateCategoryRequestDto;
import com.sparta.spartaproject.dto.response.CategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    Category toCategory(CreateCategoryRequestDto request);
}