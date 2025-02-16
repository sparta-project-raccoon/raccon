package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.OrderRequestDto;
import com.sparta.spartaproject.dto.request.OrderStatusRequestDto;
import com.sparta.spartaproject.dto.response.OrderResponseDto;
import com.sparta.spartaproject.dto.response.OrderStatusResponseDto;
import com.sparta.spartaproject.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sparta.spartaproject.domain.order.OrderStatus.*;
import static com.sparta.spartaproject.exception.ErrorCode.ORDER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final UserService userService;

    public void updateStatus(OrderStatusRequestDto request) {
        Order findedOrder = orderRepository.findByIdStatusNot(request.getOrderId(), DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        findedOrder.changeOrderStatus(request.getOrderStatus());

        orderRepository.save(findedOrder);
    }

    @Transactional(readOnly = true)
    public OrderStatusResponseDto getStatus(UUID orderId) {
        Order findedOrder = orderRepository.findByIdStatusNot(orderId, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        OrderStatusResponseDto orderStatusResponseDto = new OrderStatusResponseDto();
        orderStatusResponseDto.setOrderId(findedOrder.getId());
        orderStatusResponseDto.setOrderStatus(findedOrder.getOrderStatus().getDescription());

        return orderStatusResponseDto;
    }

    // todo 중복되는 코드 처리하기
    public void deleteOrder(UUID orderId) {
        Order findedOrder = orderRepository.findByIdStatusNot(orderId, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        findedOrder.changeOrderStatus(DELETED);
        orderRepository.save(findedOrder);
    }

    public void rejectOrder(UUID orderId) {
        Order findedOrder = orderRepository.findByIdStatusNot(orderId, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        // todo 사장님 체크
        findedOrder.changeOrderStatus(REFUSE);
    }

    public void acceptOrder(UUID orderId) {
        Order findedOrder = orderRepository.findByIdStatusNot(orderId, DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        // todo 사장님 체크하기
        findedOrder.changeOrderStatus(ACCEPT);
    }

    public void createOrder(OrderRequestDto request) {
        User user = getUser();

        Order order = Order.builder()
                .address(request.getAddress())
                .orderMethod(request.getOrderMethod())
                .orderStatus(request.getStatus())
                .request(request.getRequest())
                .total_price(request.getTotalPrice())
                .user(user)
                .build();

        OrderHistory orderHIstory = OrderHistory.builder()
                .order(order)
//                .food() // todo 음식 추가
                .price(request.getTotalPrice()) // todo 음식 추가
                .qty(1) // todo 음식 추가
                .build();

        orderHistoryRepository.save(orderHIstory);

        orderRepository.save(order);
    }

    // todo 음식 추가
    public List<OrderResponseDto> getAllOrders() {
        User user = getUser();
        List<Order> orders = orderRepository.findAllByUserStatusNot(user, DELETED);

        log.info("orders.size : {}" ,orders.size());

        List<OrderResponseDto> result = orders.stream()
                .map(o -> OrderResponseDto.builder()
                        .orderId(o.getId())
                        .orderStatus(o.getOrderStatus())
                        .totalPrice(o.getTotal_price())
//                        .storeName(o.getStore().getName())
                        .storeName("교동 짬뽕")
                        .build())
                .collect(Collectors.toList());

        return result;
    }

    private User getUser() {
        return userService.loginUser();
    }
}
