package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderHistory;
import com.sparta.spartaproject.domain.store.Store;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-22T08:50:43+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class OrderHistoryMapperImpl implements OrderHistoryMapper {

    @Override
    public OrderHistory toOrderHistory(Order order, Store store, Food food, Integer qty, Integer price) {
        if ( order == null && store == null && food == null && qty == null && price == null ) {
            return null;
        }

        OrderHistory.OrderHistoryBuilder<?, ?> orderHistory = OrderHistory.builder();

        if ( order != null ) {
            orderHistory.order( order );
            orderHistory.createdAt( order.getCreatedAt() );
            orderHistory.updatedAt( order.getUpdatedAt() );
        }
        if ( food != null ) {
            orderHistory.food( food );
            orderHistory.price( food.getPrice() );
        }
        orderHistory.store( store );
        orderHistory.qty( qty );

        return orderHistory.build();
    }
}
