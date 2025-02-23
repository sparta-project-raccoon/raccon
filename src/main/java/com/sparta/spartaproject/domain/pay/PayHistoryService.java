package com.sparta.spartaproject.domain.pay;

import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.PayMethod;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.domain.user.Role;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreatePayHistoryRequestDto;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.mapper.PayHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final StoreService storeService;

    @Transactional(readOnly = true)
    public Page<PayHistoryDetailDto> getPayHistories(Pageable customPageable) {
        Page<PayHistory> payHistories = payHistoryRepository.findPayHistoryList(customPageable);

        List<PayHistoryDetailDto> dtos = payHistories.stream().map(
            payHistoryMapper::toPayHistoryDetailDto
        ).toList();

        return new PageImpl<>(dtos, customPageable, payHistories.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PayHistoryDetailDto getPayHistory(UUID id) {
        PayHistory payHistory = getPayHistoryById(id);

        return payHistoryMapper.toPayHistoryDetailDto(payHistory);
    }

    @Transactional(readOnly = true)
    public Page<PayHistoryDetailDto> getMyPayHistories(Pageable customPageable) {
        User user = getUser();

        if (user.getRole() != Role.CUSTOMER) {
            throw new BusinessException(FORBIDDEN);
        }

        Page<PayHistory> payHistories = payHistoryRepository.findPayHistoryListByUserId(customPageable, user.getId());

        List<PayHistoryDetailDto> payHistoryDetailDtoList = payHistories.stream().map(
            payHistoryMapper::toPayHistoryDetailDto
        ).toList();

        return new PageImpl<>(payHistoryDetailDtoList, customPageable, payHistories.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<PayHistoryDetailDto> getPayHistoriesForOwner(Pageable customPageable) {
        User user = getUser();
        Store store = storeService.getStoreByOwnerId(user.getId());

        if (user.getRole() != Role.OWNER) {
            throw new BusinessException(FORBIDDEN);
        }

        Page<PayHistory> payHistories = payHistoryRepository.findPayHistoryListByStoreId(customPageable, store.getId());

        List<PayHistoryDetailDto> payHistoryDetailDtoList = payHistories.stream().map(
            payHistoryMapper::toPayHistoryDetailDto
        ).toList();

        return new PageImpl<>(payHistoryDetailDtoList, customPageable, payHistories.getTotalElements());
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