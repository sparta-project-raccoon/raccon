package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreRepository;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.request.UpdateOrderStatusRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import com.sparta.spartaproject.dto.response.OrderStatusDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.mapper.OrderHistoryMapper;
import com.sparta.spartaproject.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.sparta.spartaproject.domain.order.OrderStatus.CANCEL;
import static com.sparta.spartaproject.domain.order.OrderStatus.REFUSE;
import static com.sparta.spartaproject.domain.user.Role.CUSTOMER;
import static com.sparta.spartaproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    // todo 중복되는 코드 처리하기
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final OrderHistoryMapper orderHistoryMapper;
    private final OrderHistoryRepository orderHistoryRepository;

    private final Integer size = 10;

    @Transactional
    public void updateStatus(UpdateOrderStatusRequestDto request) {
        Order findedOrder = orderRepository.findByIdAndIsDeletedFalse(request.orderId())
            .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        User user = getUser();

        if (user.getRole() == CUSTOMER) {
            findedOrder.changeOrderStatus(REFUSE);
        }
    }

    @Transactional(readOnly = true)
    public OrderStatusDto getStatus(UUID orderId) {
        Order findedOrder = orderRepository.findByIdAndIsDeletedFalse(orderId)
            .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        return orderMapper.toOrderStatusResponseDto(findedOrder);
    }

    @Transactional
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
            .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
            throw new BusinessException(CAN_NOT_CANCEL_ORDER);
        }

        order.changeOrderStatus(CANCEL);
        orderRepository.save(order);
    }

    @Transactional
    public void rejectOrder(UUID orderId) {
        Order findedOrder = orderRepository.findByIdAndIsDeletedFalse(orderId)
            .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        User user = getUser();

        if (user.getRole() == CUSTOMER) {
            findedOrder.changeOrderStatus(REFUSE);
        }
    }

    @Transactional
    public void acceptOrder(UUID orderId) {
        Order findedOrder = orderRepository.findByIdAndIsDeletedFalse(orderId)
            .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        User user = getUser();

        if (user.getRole() == CUSTOMER) {
            findedOrder.changeOrderStatus(REFUSE);
        }
    }

    @Transactional
    public void createOrder(CreateOrderRequestDto request) {
        User user = getUser();
        Store store = storeRepository.findById(request.storeId())
            .orElseThrow(() -> new BusinessException(STORE_NOT_FOUND));

        Order order = orderMapper.toOrder(request, user, store);

        // todo 음식 수정하기
        OrderHistory orderHistory = orderHistoryMapper.toOrderHistory(order, store, UUID.randomUUID(), 1, 10000);

        orderHistoryRepository.save(orderHistory);

        orderRepository.save(order);
    }

    // todo 음식 추가
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders(int page) {
        User user = getUser();
        Pageable pageable = PageRequest.of(page - 1, size);

        // todo
        List<Order> orders = orderRepository.findAllByUserAndIsDeletedFalse(pageable, user);

        return orders.stream().map(
            orderMapper::toOrderDto
        ).toList();
    }

    @Transactional
    public OrderDetailDto getOrderDetail(UUID id) {

        Order order = orderRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        return orderMapper.toOrderDetailResponseDto(order);
    }


    private User getUser() {
        return userService.loginUser();
    }

    public Order getOrderByIdAndIsDeletedIsFalse(UUID id) {
        return orderRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
    }
}