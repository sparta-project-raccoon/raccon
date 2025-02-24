package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateFoodOrderRequestDto;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.request.UpdateOrderStatusRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.FoodMapper;
import com.sparta.spartaproject.mapper.OrderHistoryMapper;
import com.sparta.spartaproject.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.sparta.spartaproject.exception.ErrorCode.CAN_NOT_CANCEL_ORDER;
import static com.sparta.spartaproject.exception.ErrorCode.ORDER_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final FoodMapper foodMapper;
    private final OrderMapper orderMapper;
    private final StoreService storeService;
    private final CircularService circularService;
    private final OrderRepository orderRepository;
    private final OrderHistoryMapper orderHistoryMapper;
    private final OrderHistoryRepository orderHistoryRepository;

    @Transactional
    public void createOrder(CreateOrderRequestDto request) {
        User user = getUser();
        Store store = circularService.getStoreService().getStoreByIdAndIsDeletedIsFalse(request.storeId());

        if (store.getOwner().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.SELF_STORE_ORDER_NOT_ALLOWED);
        }

        Order order = orderMapper.toOrder(request, user, store, request.foods().size());

        List<OrderHistory> orderHistoryList = new ArrayList<>();
        int totalPrice = 0;

        for (CreateFoodOrderRequestDto foodDto : request.foods()) {
            Food food = circularService.getFoodService().getFoodById(foodDto.foodId());

            if (!Objects.equals(store.getId(), food.getStore().getId())) {
                throw new BusinessException(ErrorCode.FOOD_NOT_IN_STORE);
            }

            OrderHistory orderHistory = orderHistoryMapper.toOrderHistory(
                order, store, food, foodDto.quantity(), foodDto.quantity() * food.getPrice()
            );

            orderHistoryList.add(orderHistory);

            totalPrice += food.getPrice() * foodDto.quantity();
        }
        order.updateTotalPrice(totalPrice);
        orderRepository.save(order);

        circularService.getPayHistoryService().savePayHistory(
            order, store, user, request.payMethod()
        );

        orderHistoryRepository.saveAll(orderHistoryList);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getOrders(Pageable customPageable) {

        Page<Order> orders = orderRepository.findAllWithOrderHistoriesAndFoods(customPageable);

        List<OrderDto> ordersDto = orders.getContent().stream().map(
            order -> orderMapper.toOrderDto(
                order,
                order.getOrderHistories().stream().map(orderHistory ->
                    foodMapper.toFoodQtySummaryDto(
                        orderHistory.getQty(),
                        foodMapper.toFoodSummaryDto(orderHistory.getFood())
                    )
                ).toList()
            )
        ).toList();

        return new PageImpl<>(ordersDto, customPageable, orders.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getOrdersForOwner(UUID storeId, Pageable customPageable) {
        User owner = getUser();
        Store store = storeService.getStoreById(storeId);

        if (!store.getOwner().getId().equals(owner.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        Page<Order> orders = orderRepository.findAllWithOrderHistoriesAndFoodsByStoreId(store.getId(), customPageable);

        List<OrderDto> ordersDto = orders.getContent().stream().map(
            order -> orderMapper.toOrderDto(
                order,
                order.getOrderHistories().stream().map(orderHistory ->
                    foodMapper.toFoodQtySummaryDto(
                        orderHistory.getQty(),
                        foodMapper.toFoodSummaryDto(orderHistory.getFood())
                    )
                ).toList()
            )
        ).toList();

        return new PageImpl<>(ordersDto, customPageable, orders.getTotalElements());
    }

    @Transactional
    public OrderDetailDto getOrderDetail(UUID id) {
        User user = getUser();
        Order order = orderRepository.findByOrderId(id);

        if (user.getRole() == Role.CUSTOMER && !order.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return orderMapper.toOrderDetailResponseDto(
            order,
            order.getOrderHistories().stream().map(orderHistory ->
                foodMapper.toFoodQtySummaryDto(
                    orderHistory.getQty(),
                    foodMapper.toFoodSummaryDto(orderHistory.getFood())
                )
            ).toList()
        );
    }

    @Transactional
    public void acceptOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.updateStatus(OrderStatus.ACCEPT);

        log.info("주문: {}, 주문 수락", orderId);
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        Order order = getOrderById(orderId);

        if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            order.updateStatus(OrderStatus.PENDING);
            log.info("주문: {}, 주문 취소 시간 초과로 취소불가", orderId);
            throw new BusinessException(CAN_NOT_CANCEL_ORDER);
        }

        order.updateStatus(OrderStatus.CANCEL);
        order.deleteOrder();

        log.info("주문: {}, 취소 완료", orderId);
        orderRepository.save(order);
    }

    @Transactional
    public void rejectOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        order.updateStatus(OrderStatus.REFUSE);

        log.info("주문: {}, 주문 거절", orderId);
    }

    @Transactional
    public void updateStatus(UpdateOrderStatusRequestDto update) {
        Order order = getOrderById(update.orderId());
        order.updateStatus(update.status());

        log.info("주문: {}, 주문 상태 변경 완료", update.orderId());
    }


    private User getUser() {
        return circularService.getUserService().loginUser();
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findByIdAndIsDeletedIsFalse(orderId)
            .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
    }
}