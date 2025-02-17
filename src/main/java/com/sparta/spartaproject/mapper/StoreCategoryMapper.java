package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoreCategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", source = "store")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StoreCategory toStoreCategory(Store store, Category category);
}