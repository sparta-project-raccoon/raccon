package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.PayMethod;
import com.sparta.spartaproject.domain.pay.PayHistory;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-23T11:04:24+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class PayHistoryMapperImpl implements PayHistoryMapper {

    @Override
    public PayHistory toPayHistory(Order order, Store store, User user, PayMethod payMethod) {
        if ( order == null && store == null && user == null && payMethod == null ) {
            return null;
        }

        PayHistory.PayHistoryBuilder<?, ?> payHistory = PayHistory.builder();

        if ( order != null ) {
            payHistory.order( order );
            payHistory.user( order.getUser() );
        }
        payHistory.store( store );
        payHistory.payMethod( payMethod );

        return payHistory.build();
    }

    @Override
    public PayHistoryDetailDto toPayHistoryDetailDto(PayHistory payHistory) {
        if ( payHistory == null ) {
            return null;
        }

        UUID orderId = null;
        UUID storeId = null;
        String storeName = null;
        Integer totalPrice = null;
        UUID id = null;
        PayMethod payMethod = null;
        LocalDateTime createdAt = null;

        orderId = payHistoryOrderId( payHistory );
        storeId = payHistoryOrderStoreId( payHistory );
        storeName = payHistoryOrderStoreName( payHistory );
        totalPrice = payHistoryOrderTotalPrice( payHistory );
        id = payHistory.getId();
        payMethod = payHistory.getPayMethod();
        createdAt = payHistory.getCreatedAt();

        PayHistoryDetailDto payHistoryDetailDto = new PayHistoryDetailDto( id, orderId, storeId, storeName, totalPrice, payMethod, createdAt );

        return payHistoryDetailDto;
    }

    private UUID payHistoryOrderId(PayHistory payHistory) {
        if ( payHistory == null ) {
            return null;
        }
        Order order = payHistory.getOrder();
        if ( order == null ) {
            return null;
        }
        UUID id = order.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID payHistoryOrderStoreId(PayHistory payHistory) {
        if ( payHistory == null ) {
            return null;
        }
        Order order = payHistory.getOrder();
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

    private String payHistoryOrderStoreName(PayHistory payHistory) {
        if ( payHistory == null ) {
            return null;
        }
        Order order = payHistory.getOrder();
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

    private Integer payHistoryOrderTotalPrice(PayHistory payHistory) {
        if ( payHistory == null ) {
            return null;
        }
        Order order = payHistory.getOrder();
        if ( order == null ) {
            return null;
        }
        Integer totalPrice = order.getTotalPrice();
        if ( totalPrice == null ) {
            return null;
        }
        return totalPrice;
    }
}
