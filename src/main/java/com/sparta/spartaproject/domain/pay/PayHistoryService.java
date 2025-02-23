package com.sparta.spartaproject.domain.pay;

import com.sparta.spartaproject.common.SortUtils;
import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.PayMethod;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreatePayHistoryRequestDto;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.mapper.PayHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.sparta.spartaproject.domain.pay.PayStatus.CANCELLED;
import static com.sparta.spartaproject.domain.pay.PayStatus.COMPLETED;
import static com.sparta.spartaproject.exception.ErrorCode.FORBIDDEN;
import static com.sparta.spartaproject.exception.ErrorCode.PAY_HISTORY_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayHistoryService {
    private final UserService userService;
    private final CircularService circularService;
    private final PayHistoryMapper payHistoryMapper;
    private final PayHistoryRepository payHistoryRepository;

    private final Integer size = 10;

    @Transactional(readOnly = true)
    public Page<PayHistoryDetailDto> getPayHistories(int page, String sortDirection) {
        Sort sort = SortUtils.getSort(sortDirection);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<PayHistory> payhistories = payHistoryRepository.findPayHistoryList(pageable);

        List<PayHistoryDetailDto> dtos = payhistories.stream().map(
            payHistoryMapper::toPayHistoryDetailDto
        ).toList();

        return new PageImpl<>(dtos, pageable, payhistories.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PayHistoryDetailDto getPayHistory(UUID id) {
        PayHistory payHistory = getPayHistoryById(id);

        return payHistoryMapper.toPayHistoryDetailDto(payHistory);
    }

    @Transactional(readOnly = true)
    public Page<PayHistoryDetailDto> getMyPayHistories(int page, String sortDirection) {
        Sort sort = SortUtils.getSort(sortDirection);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        User user = getUser();
        if (user.getRole() != Role.CUSTOMER) {
            throw new BusinessException(FORBIDDEN);
        }

        Page<PayHistory> payhistories = payHistoryRepository.findPayHistoryListByUserId(pageable, user.getId());

        List<PayHistoryDetailDto> dtos = payhistories.stream().map(
            payHistoryMapper::toPayHistoryDetailDto
        ).toList();

        return new PageImpl<>(dtos, pageable, payhistories.getTotalElements());
    }

    @Transactional
    public void createPayHistory(CreatePayHistoryRequestDto request) {
        User user = getUser();
        Order order = circularService.getOrderService().getOrderById(request.orderId());

        if (!Objects.equals(order.getUser(), user)) {
            throw new BusinessException(FORBIDDEN);
        }

        PayHistory payHistory = payHistoryMapper.toPayHistory(order, order.getStore(), user, order.getPayMethod());
        payHistoryRepository.save(payHistory);

        log.info("결제 신청");
    }

    @Transactional
    public void completedPayHistory(UUID id) {
        PayHistory payHistory = getPayHistoryById(id);

        payHistory.updateStatus(COMPLETED);
        log.info("결제: {}, 결제 완료", id);
    }

    public void cancelledPayHistory(UUID id) {
        PayHistory payHistory = getPayHistoryById(id);

        payHistory.updateStatus(CANCELLED);
        log.info("결제: {}, 결제 취소", id);
    }


    private User getUser() {
        return userService.loginUser();
    }

    public PayHistory getPayHistoryById(UUID id) {
        return payHistoryRepository.findById(id)
            .orElseThrow(() -> new BusinessException(PAY_HISTORY_NOT_EXIST));
    }

    public void savePayHistory(Order order, Store store, User user, PayMethod payMethod) {
        PayHistory newPayHistory = payHistoryMapper.toPayHistory(order, store, user, payMethod);
        payHistoryRepository.save(newPayHistory);

        log.info("결제 내역 저장");
    }
}