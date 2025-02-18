package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.order.*;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.request.UpdateOrderStatusRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailResponseDto;
import com.sparta.spartaproject.dto.response.OrderResponseDto;
import com.sparta.spartaproject.dto.response.OrderStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Description(
        "주문하기"
    )
    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequestDto dto) {
        orderService.createOrder(dto);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 취소"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable UUID id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    @Description(
        "주문 상태 변경"
    )
    @PatchMapping("/status")
    public ResponseEntity<String> updateStatus(@RequestBody UpdateOrderStatusRequestDto request) {
        orderService.updateStatus(request);
        return ResponseEntity.ok("주문 상태가 " + request.getOrderStatus() + "로 변경되었습니다");
    }

    @Description(
        "주문 상태 조회"
    )
    @GetMapping("/{id}/status")
    public ResponseEntity<OrderStatusResponseDto> getStatus(@PathVariable UUID id) {
        OrderStatusResponseDto result = orderService.getStatus(id);
        return ResponseEntity.ok(result);
    }

    @Description(
        "주문 받기"
    )
    @PatchMapping("/{id}/accept")
    public ResponseEntity<String> acceptOrder(@PathVariable UUID id) {
        orderService.acceptOrder(id);
        return ResponseEntity.ok("주문을 받았습니다.");
    }

    @Description(
        "주문 거절"
    )
    @PatchMapping("/{id}/reject")
    public ResponseEntity<String> rejectOrder(@PathVariable UUID id) {
        orderService.rejectOrder(id);
        return ResponseEntity.ok("주문이 거절되었습니다.");
    }

    @Description(
        "주문 내역 확인"
    )
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(
        @RequestParam(required = false, defaultValue = "1") int page
    ) {

        List<OrderResponseDto> result = orderService.getAllOrders(page);

        return ResponseEntity.ok(result);
    }

    @Description(
        "주문 내역 상세 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponseDto> getOrderDetail(@PathVariable UUID id) {
        OrderDetailResponseDto result = orderService.getOrderDetail(id);

        return ResponseEntity.ok(result);
    }
}
