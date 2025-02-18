package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.dto.request.CreateCategoryRequestDto;
import com.sparta.spartaproject.dto.response.CategoryDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T22:20:02+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDto toCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        UUID id = null;
        String name = null;

        id = category.getId();
        name = category.getName();

        CategoryDto categoryDto = new CategoryDto( id, name );

        return categoryDto;
    }

    @Override
    public Category toCategory(CreateCategoryRequestDto request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder<?, ?> category = Category.builder();

        category.name( request.name() );

        return category.build();
    }
}
