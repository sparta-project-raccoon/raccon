package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderMethod;
import com.sparta.spartaproject.domain.order.OrderStatus;
import com.sparta.spartaproject.domain.order.PayMethod;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.response.FoodQtySummaryDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-23T11:04:24+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toOrder(CreateOrderRequestDto source, User user, Store store, Integer totalFoodCount) {
        if ( source == null && user == null && store == null && totalFoodCount == null ) {
            return null;
        }

        Order.OrderBuilder<?, ?> order = Order.builder();

        if ( source != null ) {
            order.address( source.address() );
            order.request( source.request() );
            order.orderMethod( source.orderMethod() );
            order.payMethod( source.payMethod() );
        }
        if ( store != null ) {
            order.store( store );
            order.createdBy( store.getCreatedBy() );
            order.updatedBy( store.getUpdatedBy() );
        }
        order.user( user );
        order.totalFoodCount( totalFoodCount );

        return order.build();
    }

    @Override
    public OrderDetailDto toOrderDetailResponseDto(Order order, List<FoodQtySummaryDto> foods) {
        if ( order == null && foods == null ) {
            return null;
        }

        Long userId = null;
        UUID storeId = null;
        String storeName = null;
        UUID id = null;
        Integer totalPrice = null;
        String request = null;
        OrderMethod orderMethod = null;
        PayMethod payMethod = null;
        String address = null;
        OrderStatus status = null;
        Integer totalFoodCount = null;
        if ( order != null ) {
            userId = orderUserId( order );
            storeId = orderStoreId( order );
            storeName = orderStoreName( order );
            id = order.getId();
            totalPrice = order.getTotalPrice();
            request = order.getRequest();
            orderMethod = order.getOrderMethod();
            payMethod = order.getPayMethod();
            address = order.getAddress();
            status = order.getStatus();
            totalFoodCount = order.getTotalFoodCount();
        }
        List<FoodQtySummaryDto> foods1 = null;
        List<FoodQtySummaryDto> list = foods;
        if ( list != null ) {
            foods1 = new ArrayList<FoodQtySummaryDto>( list );
        }

        OrderDetailDto orderDetailDto = new OrderDetailDto( id, userId, storeId, storeName, totalPrice, request, orderMethod, payMethod, address, status, totalFoodCount, foods1 );

        return orderDetailDto;
    }

    @Override
    public OrderDto toOrderDto(Order order, List<FoodQtySummaryDto> foods) {
        if ( order == null && foods == null ) {
            return null;
        }

        UUID storeId = null;
        UUID id = null;
        OrderStatus status = null;
        Integer totalPrice = null;
        Integer totalFoodCount = null;
        LocalDateTime createdAt = null;
        if ( order != null ) {
            storeId = orderStoreId( order );
            id = order.getId();
            status = order.getStatus();
            totalPrice = order.getTotalPrice();
            totalFoodCount = order.getTotalFoodCount();
            createdAt = order.getCreatedAt();
        }
        List<FoodQtySummaryDto> foods1 = null;
        List<FoodQtySummaryDto> list = foods;
        if ( list != null ) {
            foods1 = new ArrayList<FoodQtySummaryDto>( list );
        }

        OrderDto orderDto = new OrderDto( id, storeId, foods1, status, totalPrice, totalFoodCount, createdAt );

        return orderDto;
    }

    private Long orderUserId(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID orderStoreId(Order order) {
        if ( order == null ) {
            return null;
        }
        Store store = order.getStore();
        if ( store == null ) {
            return null;
        }
        UUID id = store.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String orderStoreName(Order order) {
        if ( order == null ) {
            return null;
        }
        Store store = order.getStore();
        if ( store == null ) {
            return null;
        }
        String name = store.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
