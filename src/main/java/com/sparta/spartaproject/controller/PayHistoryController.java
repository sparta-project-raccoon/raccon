package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.pay.PayHistoryService;
import com.sparta.spartaproject.dto.response.PayHistoryDto;
import com.sparta.spartaproject.dto.request.CreatePayHistoryRequestDto;
import com.sparta.spartaproject.dto.request.UpdatePayHistoryDto;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pay-history")
@RequiredArgsConstructor
public class PayHistoryController {

    private final PayHistoryService payHistoryService;

    @Description(
            "결제하기"
    )
    @PostMapping
    public ResponseEntity<Void> createPayHistory(@RequestBody CreatePayHistoryRequestDto request) {
        payHistoryService.createPayHistory(request);
        return ResponseEntity.ok().build();
    }

    @Description(
            "결제 상세 확인"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PayHistoryDetailDto> getPayHistoryDetail(@PathVariable("id") UUID payHistoryId){
        return ResponseEntity.ok(payHistoryService.getPayHistoryDetail(payHistoryId));
    }

    @Description(
            "결제 내역 리스트 확인"
    )
    @GetMapping
    public ResponseEntity<List<PayHistoryDto>> getPayHistoryList(
            @RequestParam(required = false, defaultValue = "1") int page
    ){
        return ResponseEntity.ok(payHistoryService.getPayHistoryList(page));
    }

    @Description(
            "결제 정보 수정"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePayHistory(@PathVariable("id") UUID payHistoryId, @RequestBody UpdatePayHistoryDto update){
        payHistoryService.updatePayHistory(payHistoryId, update);
        return ResponseEntity.ok().build();
    }


    @Description(
            "결제 삭제"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayHistory(@PathVariable("id") UUID payHistoryId){
        payHistoryService.deletePayHistory(payHistoryId);
        return ResponseEntity.ok().build();
    }
}
