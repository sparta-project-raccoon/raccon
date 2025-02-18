package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreRepository;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.request.UpdateOrderStatusRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailResponseDto;
import com.sparta.spartaproject.dto.response.OrderResponseDto;
import com.sparta.spartaproject.dto.response.OrderStatusResponseDto;
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

import static com.sparta.spartaproject.domain.order.OrderStatus.*;
import static com.sparta.spartaproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    // todo 중복되는 코드 처리하기
    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final StoreRepository storeRepository;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final OrderHistoryMapper orderHistoryMapper;

    private Integer size = 10;

    public void updateStatus(UpdateOrderStatusRequestDto request) {
        Order findedOrder = orderRepository.findById(request.getOrderId(), DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        User user = getUser();

        // todo 가게 사장인지 체크
        if (isManager(user)) {
            findedOrder.changeOrderStatus(request.getOrderStatus());
            orderRepository.save(findedOrder);
        }
    }

    @Transactional(readOnly = true)
    public OrderStatusResponseDto getStatus(UUID orderId) {
        Order findedOrder = orderRepository.findById(orderId, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        return orderMapper.toOrderStatusResponseDto(findedOrder);
    }


    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cancellationDeadline = now.minusMinutes(5);

        if (order.getCreatedAt().isBefore(cancellationDeadline)) {
            throw new BusinessException(CAN_NOT_CANCEL_ORDER);
        }

        order.changeOrderStatus(CANCEL);
        orderRepository.save(order);
    }

    public void rejectOrder(UUID orderId) {
        Order findedOrder = orderRepository.findById(orderId, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        User user = getUser();
        if (isManager(user)) {
            findedOrder.changeOrderStatus(REFUSE);
        }
    }

    public void acceptOrder(UUID orderId) {
        Order findedOrder = orderRepository.findById(orderId, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        User user = getUser();

        if (isManager(user)) {
            findedOrder.changeOrderStatus(ACCEPT);
        }

    }

    public void createOrder(CreateOrderRequestDto request) {
        User user = getUser();
        Store store = storeRepository.findById(request.store_id()).orElseThrow(() -> new BusinessException(STORE_NOT_FOUND));

        Order order = orderMapper.toOrder(request, user, store);

        // todo 음식 수정하기
        OrderHistory orderHistory = orderHistoryMapper.toOrderHistory(order, store, UUID.randomUUID(), 1, 10000);

        orderHistoryRepository.save(orderHistory);

        orderRepository.save(order);
    }

    // todo 음식 추가
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders(int page) {
        User user = getUser();
        Pageable pageable = PageRequest.of(page - 1, size);

        // todo
        List<Order> orders = orderRepository.findAllByUser(pageable, user, DELETED);

        return orders.stream()
                .map(orderMapper::toOrderResponseDto)
                .toList();
    }


    public OrderDetailResponseDto getOrderDetail(UUID id) {

        Order order = orderRepository.findById(id, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        return orderMapper.toOrderDetailResponseDto(order);
    }


    private User getUser() {
        return userService.loginUser();
    }

    private Boolean isManager(User user) {
        if (!(user.getRole() == Role.MANAGER || user.getRole() == Role.OWNER)) {
            throw new BusinessException(HANDLE_ACCESS_DENIED);
        }

        return true;
    }
}
