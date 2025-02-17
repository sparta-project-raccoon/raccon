package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderHistory;
import com.sparta.spartaproject.domain.store.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "food_id", source = "food_id"),
            @Mapping(target = "order", source = "order"),
            @Mapping(target = "store", source = "store"),
            @Mapping(target = "createdAt",source = "order.createdAt"),
            @Mapping(target = "updatedAt",source = "order.updatedAt"),
    })
    OrderHistory toOrderHistory(Order order, Store store, UUID food_id, Integer qty, Integer price);
}
