package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.response.FoodQtySummaryDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "store", source = "store")
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "address", source = "source.address")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalFoodCount", source = "totalFoodCount")
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toOrder(CreateOrderRequestDto source, User user, Store store, Integer totalFoodCount);

    @Mapping(target = "userId", source = "order.user.id")
    @Mapping(target = "storeId", source = "order.store.id")
    @Mapping(target = "storeName", source = "order.store.name")
    @Mapping(target = "foods", source = "foods")
    OrderDetailDto toOrderDetailResponseDto(Order order, List<FoodQtySummaryDto> foods);

    @Mapping(target = "storeId", source = "order.store.id")
    OrderDto toOrderDto(Order order, List<FoodQtySummaryDto> foods);
}
