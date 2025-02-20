package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderMethod;
import com.sparta.spartaproject.domain.order.OrderStatus;
import com.sparta.spartaproject.domain.order.PaymentMethod;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import com.sparta.spartaproject.dto.response.OrderStatusDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-19T17:49:50+0900",
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
        String orderStatus = null;

        orderId = order.getId();
        if ( order.getStatus() != null ) {
            orderStatus = order.getStatus().name();
        }

        OrderStatusDto orderStatusDto = new OrderStatusDto( orderId, orderStatus );

        return orderStatusDto;
    }

    @Override
    public Order toOrder(CreateOrderRequestDto dto, User user, Store store) {
        if ( dto == null && user == null && store == null ) {
            return null;
        }

        Order.OrderBuilder<?, ?> order = Order.builder();

        if ( dto != null ) {
            order.address( dto.address() );
            order.status( dto.status() );
            order.totalPrice( dto.totalPrice() );
            order.orderMethod( dto.orderMethod() );
            order.payMethod( dto.payMethod() );
            order.request( dto.request() );
        }
        order.user( user );
        order.store( store );

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
    public OrderDto toOrderDto(Order order) {
        if ( order == null ) {
            return null;
        }

        UUID orderId = null;
        String storeName = null;
        Integer totalPrice = null;
        OrderStatus status = null;

        orderId = order.getId();
        storeName = orderStoreName( order );
        totalPrice = order.getTotalPrice();
        status = order.getStatus();

        OrderDto orderDto = new OrderDto( orderId, status, totalPrice, storeName );

        return orderDto;
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
