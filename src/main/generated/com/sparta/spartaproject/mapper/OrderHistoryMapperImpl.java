package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderHistory;
import com.sparta.spartaproject.domain.store.Store;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-19T17:49:50+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class OrderHistoryMapperImpl implements OrderHistoryMapper {

    @Override
    public OrderHistory toOrderHistory(Order order, Store store, UUID food_id, Integer qty, Integer price) {
        if ( order == null && store == null && food_id == null && qty == null && price == null ) {
            return null;
        }

        OrderHistory.OrderHistoryBuilder<?, ?> orderHistory = OrderHistory.builder();

        if ( order != null ) {
            orderHistory.order( order );
            orderHistory.createdAt( order.getCreatedAt() );
            orderHistory.updatedAt( order.getUpdatedAt() );
        }
        orderHistory.store( store );
        orderHistory.food_id( food_id );
        orderHistory.qty( qty );
        orderHistory.price( price );

        return orderHistory.build();
    }
}
