package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.response.UserInfoDto;
import com.sparta.spartaproject.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static com.sparta.spartaproject.domain.order.OrderStatus.*;
import static com.sparta.spartaproject.exception.ErrorCode.ORDER_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public void updateStatus(OrderStatusRequestDto request) {
        Order findedOrder = orderRepository.findByIdStatusNot(request.getOrderId(),DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        findedOrder.changeOrderStatus(request.getOrderStatus());

        orderRepository.save(findedOrder);
    }

    @Transactional(readOnly = true)
    public OrderStatusResponseDto getStatus(UUID orderId) {
        Order findedOrder = orderRepository.findByIdStatusNot(orderId,DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        OrderStatusResponseDto orderStatusResponseDto = new OrderStatusResponseDto();
        orderStatusResponseDto.setOrderId(findedOrder.getId());
        orderStatusResponseDto.setOrderStatus(findedOrder.getOrderStatus().getDescription());

        return orderStatusResponseDto;
    }

    // todo 중복되는 코드 처리하기
    public void deleteOrder(UUID orderId) {
        Order findedOrder = orderRepository.findByIdStatusNot(orderId,DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        findedOrder.changeOrderStatus(DELETED);
        orderRepository.save(findedOrder);
    }

    public void rejectOrder(UUID orderId) {
        Order findedOrder = orderRepository.findByIdStatusNot(orderId,DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        // todo 사장님 체크
        findedOrder.changeOrderStatus(REFUSE);
    }

    public void acceptOrder(UUID orderId) {
        Order findedOrder = orderRepository.findByIdStatusNot(orderId,DELETED).orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));
        // todo 사장님 체크하기
        findedOrder.changeOrderStatus(ACCEPT);
    }

    // todo 여기서 user 정보를 가져오는게 맞는지 모르겟다
    public void createOrder(OrderRequestDto request) {
        User user  = getUser();
        Order order = Order.builder()
                .address(request.getAddress())
                .orderMethod(request.getOrderMethod())
                .orderStatus(request.getStatus())
                .request(request.getRequest())
                .total_price(BigDecimal.valueOf(request.getTotalPrice()))
                .userId(user)
                .build();

        orderRepository.save(order);
    }

    private User getUser(){
        return userService.loginUser();
    }
}
