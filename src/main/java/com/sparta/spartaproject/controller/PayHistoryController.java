package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.common.pageable.PageableConfig;
import com.sparta.spartaproject.domain.pay.PayHistoryService;
import com.sparta.spartaproject.dto.request.CreatePayHistoryRequestDto;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pay/histories")
@RequiredArgsConstructor
public class PayHistoryController {
    private final PayHistoryService payHistoryService;
    private final PageableConfig pageableConfig;

    @Description(
        "결제 내역 전체 조회"
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Page<PayHistoryDetailDto>> getPayHistories(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(payHistoryService.getPayHistories(customPageable));
    }

    @Description(
        "결제 상세 확인"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PayHistoryDetailDto> getPayHistory(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(payHistoryService.getPayHistory(id));
    }

    @Description(
        "customer - 내 결제 내역 전체 확인하기"
    )
    @GetMapping("/me")
    public ResponseEntity<Page<PayHistoryDetailDto>> getMyPayHistories(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(payHistoryService.getMyPayHistories(customPageable));
    }

    @Description(
        "owner - 내 가게 결제 내역 조회하기"
    )
    @GetMapping("/owner")
    public ResponseEntity<Page<PayHistoryDetailDto>> getPayHistoriesForOwner(
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false) Integer size,
        @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        Pageable customPageable = pageableConfig.customPageable(page, size, sortDirection);
        return ResponseEntity.ok(payHistoryService.getPayHistoriesForOwner(customPageable));
    }

    @Description(
        "결제하기"
    )
    @PostMapping
    public ResponseEntity<Void> createPayHistory(@RequestBody CreatePayHistoryRequestDto request) {
        payHistoryService.createPayHistory(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "결제하기 - 완료"
    )
    @PatchMapping("/{id}/completed")
    public ResponseEntity<Void> completedPayHistory(@PathVariable("id") UUID id) {
        payHistoryService.completedPayHistory(id);
        return ResponseEntity.ok().build();
    }

    @Description(
        "결제 삭제(취소)"
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'MANAGER')")
    public ResponseEntity<Void> cancelledPayHistory(@PathVariable("id") UUID id) {
        payHistoryService.cancelledPayHistory(id);
        return ResponseEntity.ok().build();
    }
}