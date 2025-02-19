package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.request.CreateFoodRequestDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-20T01:34:00+0900",
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
}
