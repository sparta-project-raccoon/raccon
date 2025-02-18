package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.dto.response.FoodInfoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    FoodInfoDto toFoodInfoDto(Food food);

}
