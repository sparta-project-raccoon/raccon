package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderHistory;
import com.sparta.spartaproject.domain.store.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "food", source = "food")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "store", source = "store")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderHistory toOrderHistory(Order order, Store store, Food food, Integer qty, Integer price);
}