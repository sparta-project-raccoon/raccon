package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    @Mapping(target = "id", source = "store.id")
    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "imageUrls", source = "imageUrls")
    StoreDetailDto toStoreDetailDto(List<CategoryDto> categories, List<String> imageUrls, Store store);

    @Mapping(target = "imageUrls", source = "imageUrls")
    OnlyStoreDto toOnlyStoreDto(Store store, List<String> imageUrls);

    @Mapping(target = "stores", source = "source")
    StoreDto toStoreDto(List<StoreDetailDto> source, Integer currentPage, Integer totalPages, Integer totalElements);

    @Mapping(target = "id", source = "store.id")
    @Mapping(target = "name", source = "store.name")
    @Mapping(target = "category", source = "categoryDto")
    StoreByCategoryDto toStoreByCategoryDto(CategoryDto categoryDto, List<String> imageUrls, Store store);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "user")
    @Mapping(target = "name", source = "source.name")
    @Mapping(target = "address", source = "source.address")
    @Mapping(target = "status", source = "source.status")
    @Mapping(target = "openTime", source = "openTime")
    @Mapping(target = "closeTime", source = "closeTime")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Store toStore(CreateStoreRequestDto source, User user, LocalTime openTime, LocalTime closeTime);
}