package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", source = "store")
    @Mapping(target = "name", source = "source.name")
    @Mapping(target = "description", source = "descriptionForGemini")
    @Mapping(target = "imagePath", source = "imagePathForFood")
    @Mapping(target = "status", source = "source.status")
    @Mapping(target = "isDisplayed", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Food toFood(CreateFoodRequestDto source, Store store, String descriptionForGemini, String imagePathForFood);
}