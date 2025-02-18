package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.order.OrderService;
import com.sparta.spartaproject.dto.request.CreateOrderRequestDto;
import com.sparta.spartaproject.dto.request.UpdateOrderStatusRequestDto;
import com.sparta.spartaproject.dto.response.OrderDetailDto;
import com.sparta.spartaproject.dto.response.OrderDto;
import com.sparta.spartaproject.dto.response.OrderStatusDto;
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
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequestDto request) {
        orderService.createOrder(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 취소"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 상태 변경"
    )
    @PatchMapping("/status")
    public ResponseEntity<Void> updateStatus(@RequestBody UpdateOrderStatusRequestDto update) {
        orderService.updateStatus(update);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 상태 조회"
    )
    @GetMapping("/{id}/status")
    public ResponseEntity<OrderStatusDto> getStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getStatus(id));
    }

    @Description(
        "주문 받기"
    )
    @PatchMapping("/{id}/accept")
    public ResponseEntity<Void> acceptOrder(@PathVariable UUID id) {
        orderService.acceptOrder(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 거절"
    )
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable UUID id) {
        orderService.rejectOrder(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "주문 내역 확인"
    )
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(
        @RequestParam(required = false, defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(orderService.getAllOrders(page));
    }

    @Description(
        "주문 내역 상세 조회"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderDetail(id));
    }
}
