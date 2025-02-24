package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.response.FoodDetailDto;
import com.sparta.spartaproject.dto.response.FoodQtySummaryDto;
import com.sparta.spartaproject.dto.response.FoodSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", source = "store")
    @Mapping(target = "name", source = "source.name")
    @Mapping(target = "description", source = "descriptionForGemini")
    @Mapping(target = "status", source = "source.status")
    @Mapping(target = "isDisplayed", expression = "java(Boolean.TRUE)")
    @Mapping(target = "isDeleted", expression = "java(Boolean.FALSE)")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Food toFood(CreateFoodRequestDto source, Store store, String descriptionForGemini);

    @Mapping(target = "imagePath", source = "imageUrl")
    FoodDetailDto toFoodDetailDto(Food food, String imageUrl);

    FoodSummaryDto toFoodSummaryDto(Food food);

    @Mapping(target = "qty", source = "qty")
    @Mapping(target = "food", source = "food")
    FoodQtySummaryDto toFoodQtySummaryDto(Integer qty, FoodSummaryDto food);
}