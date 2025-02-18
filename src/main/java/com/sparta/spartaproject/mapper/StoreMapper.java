package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateStoreRequestDto;
import com.sparta.spartaproject.dto.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    @Mapping(target = "id", source = "store.id")
    @Mapping(target = "categories", source = "categories")
    StoreDetailDto toStoreDetailDto(List<CategoryDto> categories, Store store);

    OnlyStoreDto toOnlyStoreDto(Store store);

    @Mapping(target = "stores", source = "source")
    StoreDto toStoreDto(List<StoreDetailDto> source, Integer currentPage, Integer totalPages, Integer totalElements);

    StoreByCategoryDto toStoreByCategoryDto(
        List<OnlyStoreDto> stores,
        Integer currentPage,
        Integer totalPages,
        Integer totalElements
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "user")
    @Mapping(target = "name", source = "source.name")
    @Mapping(target = "address", source = "source.address")
    @Mapping(target = "status", source = "source.status")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Store toStore(CreateStoreRequestDto source, User user);
}