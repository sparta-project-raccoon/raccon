package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.food.Status;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.response.FoodDetailDto;
import com.sparta.spartaproject.dto.response.FoodDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-22T08:50:43+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class FoodMapperImpl implements FoodMapper {

    @Override
    public Food toFood(CreateFoodRequestDto source, Store store, String descriptionForGemini, String imagePathForFood) {
        if ( source == null && store == null && descriptionForGemini == null && imagePathForFood == null ) {
            return null;
        }

        Food.FoodBuilder<?, ?> food = Food.builder();

        if ( source != null ) {
            food.name( source.name() );
            food.status( source.status() );
            food.price( source.price() );
        }
        food.store( store );
        food.description( descriptionForGemini );
        food.imagePath( imagePathForFood );

        return food.build();
    }

    @Override
    public FoodDto toFoodDto(List<FoodDetailDto> foods, Integer page, Integer totalPages) {
        if ( foods == null && page == null && totalPages == null ) {
            return null;
        }

        List<FoodDetailDto> foods1 = null;
        List<FoodDetailDto> list = foods;
        if ( list != null ) {
            foods1 = new ArrayList<FoodDetailDto>( list );
        }
        Integer currentPage = null;
        currentPage = page;
        Integer totalPages1 = null;
        totalPages1 = totalPages;

        FoodDto foodDto = new FoodDto( foods1, currentPage, totalPages1 );

        return foodDto;
    }

    @Override
    public FoodDetailDto toFoodDetailDto(Food food, String imageUrl) {
        if ( food == null && imageUrl == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        Integer price = null;
        String description = null;
        Status status = null;
        if ( food != null ) {
            id = food.getId();
            name = food.getName();
            price = food.getPrice();
            description = food.getDescription();
            status = food.getStatus();
        }
        String imagePath = null;
        imagePath = imageUrl;

        FoodDetailDto foodDetailDto = new FoodDetailDto( id, name, price, description, imagePath, status );

        return foodDetailDto;
    }
}
