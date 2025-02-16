package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.order.OrderRequestDto;
import com.sparta.spartaproject.domain.order.OrderService;
import com.sparta.spartaproject.domain.order.OrderStatusRequestDto;
import com.sparta.spartaproject.domain.order.OrderStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody OrderRequestDto dto){
        orderService.createOrder(dto);
        return ResponseEntity.ok().build();
    }

    // todo 주문 취소 - 등록 후 5분 전까지 가능, 지나면 시간 지나서 안된다고 알려주기
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    // 주문 상태 변경
    @PatchMapping("/status")
    public ResponseEntity<String> updateStatus(@RequestBody OrderStatusRequestDto request) {
        orderService.updateStatus(request);
        return ResponseEntity.ok("주문 상태가 변경되었습니다.");
    }

    // todo 주문 상태 조회 -> 나중에 여러 개 조회할 수 있으니
    @GetMapping("/{id}/status")
    public ResponseEntity<OrderStatusResponseDto> getStatus(@PathVariable UUID id) {
        OrderStatusResponseDto result = orderService.getStatus(id);
        return ResponseEntity.ok(result);
    }

    // todo 주문 받기 - 사장님인지 판단
    @PatchMapping("/{id}/accept")
    public ResponseEntity<String> acceptOrder(@PathVariable UUID id) {
        orderService.acceptOrder(id);
        return ResponseEntity.ok("주문을 받았습니다.");
    }

    // todo 주문 거절
    @PatchMapping("/{id}/reject")
    public ResponseEntity<String> rejectOrder(@PathVariable UUID id) {
        orderService.rejectOrder(id);
        return ResponseEntity.ok("주문이 거절되었습니다.");
    }

    // 아래 애들은 주문내역 테이블 만들고 나서
    // todo 주문 내역 확인
    // todo 주문 내역 상세 조회

}
