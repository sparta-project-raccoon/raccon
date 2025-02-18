package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.pay.PayHistory;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreatePayHistoryRequestDto;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import com.sparta.spartaproject.dto.response.PayHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PayHistoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "paymentMethod", source = "request.paymentMethod")
    PayHistory toPayHistory(CreatePayHistoryRequestDto request, Store store, Order order, User user);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "shopName", source = "order.store.name")
    @Mapping(target = "totalPrice", source = "order.totalPrice")
    @Mapping(target = "paymentMethod", source = "paymentMethod.description")
    PayHistoryDetailDto toPayHistoryDetailDto(PayHistory payHistory);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "shopName", source = "order.store.name")
    @Mapping(target = "totalPrice", source = "order.totalPrice")
    PayHistoryDto toPayHistoryDto(PayHistory payHistory);
}
