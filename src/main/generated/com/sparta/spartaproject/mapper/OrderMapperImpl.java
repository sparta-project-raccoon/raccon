package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderMethod;
import com.sparta.spartaproject.domain.order.OrderStatus;
import com.sparta.spartaproject.domain.order.PaymentMethod;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.response.OnlyOrderDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import com.sparta.spartaproject.dto.response.OrderStatusDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-20T23:24:17+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderStatusDto toOrderStatusResponseDto(Order order) {
        if ( order == null ) {
            return null;
        }

        UUID orderId = null;
        String orderStatusDescription = null;

        orderId = order.getId();
        orderStatusDescription = orderStatusDescription( order );

        OrderStatusDto orderStatusDto = new OrderStatusDto( orderId, orderStatusDescription );

        return orderStatusDto;
    }

    @Override
    public Order toOrder(CreateOrderRequestDto dto, User user, Store store, OrderStatus status) {
        if ( dto == null && user == null && store == null && status == null ) {
            return null;
        }

        Order.OrderBuilder<?, ?> order = Order.builder();

        if ( dto != null ) {
            order.address( dto.address() );
            order.orderMethod( dto.orderMethod() );
            order.payMethod( dto.payMethod() );
            order.request( dto.request() );
        }
        if ( store != null ) {
            order.store( store );
            order.createdBy( store.getCreatedBy() );
            order.updatedBy( store.getUpdatedBy() );
        }
        order.user( user );
        order.status( status );

        return order.build();
    }

    @Override
    public OrderDetailDto toOrderDetailResponseDto(Order order) {
        if ( order == null ) {
            return null;
        }

        UUID orderId = null;
        String storeName = null;
        OrderStatus status = null;
        Integer totalPrice = null;
        OrderMethod orderMethod = null;
        PaymentMethod payMethod = null;
        String address = null;
        String request = null;

        orderId = order.getId();
        storeName = orderStoreName( order );
        status = order.getStatus();
        totalPrice = order.getTotalPrice();
        orderMethod = order.getOrderMethod();
        payMethod = order.getPayMethod();
        address = order.getAddress();
        request = order.getRequest();

        OrderDetailDto orderDetailDto = new OrderDetailDto( orderId, status, totalPrice, storeName, orderMethod, payMethod, address, request );

        return orderDetailDto;
    }

    @Override
    public OnlyOrderDto toOrderOnlyDto(Order order, String foodName, Integer totalFoodCnt) {
        if ( order == null && foodName == null && totalFoodCnt == null ) {
            return null;
        }

        UUID orderId = null;
        String storeName = null;
        Integer totalPrice = null;
        OrderStatus status = null;
        LocalDateTime createdAt = null;
        if ( order != null ) {
            orderId = order.getId();
            storeName = orderStoreName( order );
            totalPrice = order.getTotalPrice();
            status = order.getStatus();
            createdAt = order.getCreatedAt();
        }
        String foodName1 = null;
        foodName1 = foodName;
        Integer totalFoodCnt1 = null;
        totalFoodCnt1 = totalFoodCnt;

        OnlyOrderDto onlyOrderDto = new OnlyOrderDto( orderId, status, totalPrice, storeName, foodName1, totalFoodCnt1, createdAt );

        return onlyOrderDto;
    }

    @Override
    public OrderDto toOrderDto(List<OnlyOrderDto> onlyOrderDtoList, int currentPage, int totalPages, int totalElements) {
        if ( onlyOrderDtoList == null ) {
            return null;
        }

        List<OnlyOrderDto> onlyOrderDtoList1 = null;
        List<OnlyOrderDto> list = onlyOrderDtoList;
        if ( list != null ) {
            onlyOrderDtoList1 = new ArrayList<OnlyOrderDto>( list );
        }
        Integer currentPage1 = null;
        currentPage1 = currentPage;
        Integer totalPages1 = null;
        totalPages1 = totalPages;
        Integer totalElements1 = null;
        totalElements1 = totalElements;

        OrderDto orderDto = new OrderDto( onlyOrderDtoList1, currentPage1, totalPages1, totalElements1 );

        return orderDto;
    }

    private String orderStatusDescription(Order order) {
        if ( order == null ) {
            return null;
        }
        OrderStatus status = order.getStatus();
        if ( status == null ) {
            return null;
        }
        String description = status.getDescription();
        if ( description == null ) {
            return null;
        }
        return description;
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
