package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderStatus;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import com.sparta.spartaproject.dto.response.OrderStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "orderStatusDescription", source = "status.description")
    OrderStatusDto toOrderStatusResponseDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "store", source = "store")
    @Mapping(target = "address", source = "dto.address")
    @Mapping(target = "status", source = "status")
    Order toOrder(CreateOrderRequestDto dto, User user, Store store, OrderStatus status);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "storeName", source = "order.store.name")
    OrderDetailDto toOrderDetailResponseDto(Order order);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "storeName", source = "order.store.name")
    @Mapping(target = "totalPrice", source = "order.totalPrice")
    OrderDto toOrderDto(Order order,String foodName,Integer foodCnt);
}
