package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.pay.PayHistory;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreatePayHistoryRequestDto;
import com.sparta.spartaproject.dto.response.OnlyPayHistoryDto;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import com.sparta.spartaproject.dto.response.PayHistoryDto;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-20T23:24:16+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class PayHistoryMapperImpl implements PayHistoryMapper {

    @Override
    public PayHistory toPayHistory(CreatePayHistoryRequestDto request, Order order, Store store, User user) {
        if ( request == null && order == null && store == null && user == null ) {
            return null;
        }

        PayHistory.PayHistoryBuilder<?, ?> payHistory = PayHistory.builder();

        if ( request != null ) {
            payHistory.paymentMethod( request.paymentMethod() );
        }
        if ( order != null ) {
            payHistory.order( order );
            payHistory.user( order.getUser() );
        }
        payHistory.store( store );

        return payHistory.build();
    }

    @Override
    public PayHistoryDetailDto toPayHistoryDetailDto(PayHistory payHistory, String description, String payMethodDescription) {
        if ( payHistory == null && description == null && payMethodDescription == null ) {
            return null;
        }

        UUID orderId = null;
        String shopName = null;
        Integer totalPrice = null;
        if ( payHistory != null ) {
            orderId = payHistoryOrderId( payHistory );
            shopName = payHistoryOrderStoreName( payHistory );
            totalPrice = payHistoryOrderTotalPrice( payHistory );
        }
        String payStatusDescription = null;
        payStatusDescription = description;
        String paymentMethod = null;
        paymentMethod = payMethodDescription;

        PayHistoryDetailDto payHistoryDetailDto = new PayHistoryDetailDto( orderId, shopName, totalPrice, paymentMethod, payStatusDescription );

        return payHistoryDetailDto;
    }

    @Override
    public PayHistoryDto toPayHistoryDto(List<OnlyPayHistoryDto> onlyPayHistoryDtoList, Integer currentPage, Integer totalPages, Integer totalElements) {
        if ( onlyPayHistoryDtoList == null && currentPage == null && totalPages == null && totalElements == null ) {
            return null;
        }

        List<OnlyPayHistoryDto> onlyPayHistoryDtoList1 = null;
        List<OnlyPayHistoryDto> list = onlyPayHistoryDtoList;
        if ( list != null ) {
            onlyPayHistoryDtoList1 = new ArrayList<OnlyPayHistoryDto>( list );
        }
        Integer currentPage1 = null;
        currentPage1 = currentPage;
        Integer totalPages1 = null;
        totalPages1 = totalPages;
        Integer totalElements1 = null;
        totalElements1 = totalElements;

        PayHistoryDto payHistoryDto = new PayHistoryDto( onlyPayHistoryDtoList1, currentPage1, totalPages1, totalElements1 );

        return payHistoryDto;
    }

    @Override
    public OnlyPayHistoryDto toOnlyPayHistoryDto(PayHistory payHistory, String description, String payMethodDescription) {
        if ( payHistory == null && description == null && payMethodDescription == null ) {
            return null;
        }

        UUID orderId = null;
        String shopName = null;
        Integer totalPrice = null;
        if ( payHistory != null ) {
            orderId = payHistoryOrderId( payHistory );
            shopName = payHistoryStoreName( payHistory );
            totalPrice = payHistoryOrderTotalPrice( payHistory );
        }
        String status = null;
        status = description;
        String paymentMethod = null;
        paymentMethod = payMethodDescription;

        OnlyPayHistoryDto onlyPayHistoryDto = new OnlyPayHistoryDto( orderId, shopName, totalPrice, paymentMethod, status );

        return onlyPayHistoryDto;
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

    private String payHistoryStoreName(PayHistory payHistory) {
        if ( payHistory == null ) {
            return null;
        }
        Store store = payHistory.getStore();
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
