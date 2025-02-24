package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.food.Status;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import com.sparta.spartaproject.dto.response.FoodDetailDto;
import com.sparta.spartaproject.dto.response.FoodQtySummaryDto;
import com.sparta.spartaproject.dto.response.FoodSummaryDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-24T14:45:38+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class FoodMapperImpl implements FoodMapper {

    @Override
    public Food toFood(CreateFoodRequestDto source, Store store, String descriptionForGemini) {
        if ( source == null && store == null && descriptionForGemini == null ) {
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
        food.isDisplayed( Boolean.TRUE );
        food.isDeleted( Boolean.FALSE );

        return food.build();
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
        Boolean isDisplayed = null;
        if ( food != null ) {
            id = food.getId();
            name = food.getName();
            price = food.getPrice();
            description = food.getDescription();
            status = food.getStatus();
            isDisplayed = food.getIsDisplayed();
        }
        String imagePath = null;
        imagePath = imageUrl;

        FoodDetailDto foodDetailDto = new FoodDetailDto( id, name, price, description, imagePath, status, isDisplayed );

        return foodDetailDto;
    }

    @Override
    public FoodSummaryDto toFoodSummaryDto(Food food) {
        if ( food == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        Integer price = null;

        id = food.getId();
        name = food.getName();
        price = food.getPrice();

        FoodSummaryDto foodSummaryDto = new FoodSummaryDto( id, name, price );

        return foodSummaryDto;
    }

    @Override
    public FoodQtySummaryDto toFoodQtySummaryDto(Integer qty, FoodSummaryDto food) {
        if ( qty == null && food == null ) {
            return null;
        }

        Integer qty1 = null;
        qty1 = qty;
        FoodSummaryDto food1 = null;
        food1 = food;

        FoodQtySummaryDto foodQtySummaryDto = new FoodQtySummaryDto( qty1, food1 );

        return foodQtySummaryDto;
    }
}
