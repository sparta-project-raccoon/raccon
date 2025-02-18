package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailResponseDto;
import com.sparta.spartaproject.dto.response.OrderResponseDto;
import com.sparta.spartaproject.dto.response.OrderStatusResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "orderId", source = "id"),
            @Mapping(target = "orderStatus", source = "status")
    })
    OrderStatusResponseDto toOrderStatusResponseDto(Order order);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "user",source = "user"),
            @Mapping(target = "store",source = "store"),
            @Mapping(target = "address", source = "dto.address"),
            @Mapping(target = "status", source = "dto.status")
    })
    Order toOrder(CreateOrderRequestDto dto, User user, Store store);

    @Mappings({
            @Mapping(target = "orderId", source = "order.id"),
            @Mapping(target = "storeName", source = "order.store.name")
    })
    OrderDetailResponseDto toOrderDetailResponseDto(Order order);

    @Mappings({
            @Mapping(target = "orderId", source = "order.id"),
            @Mapping(target = "storeName", source = "order.store.name")
    })
    OrderResponseDto toOrderResponseDto(Order order);
}
