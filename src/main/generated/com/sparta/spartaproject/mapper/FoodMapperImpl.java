package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.food.Status;
import com.sparta.spartaproject.dto.response.FoodDetailDto;

import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-18T21:35:09+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class FoodMapperImpl implements FoodMapper {

    @Override
    public FoodDetailDto toFoodInfoDto(Food food) {
        if ( food == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        Integer price = null;
        String description = null;
        String imagePath = null;
        Status status = null;

        id = food.getId();
        name = food.getName();
        price = food.getPrice();
        description = food.getDescription();
        imagePath = food.getImagePath();
        status = food.getStatus();

        FoodDetailDto foodDetailDto = new FoodDetailDto( id, name, price, description, imagePath, status );

        return foodDetailDto;
    }
}
