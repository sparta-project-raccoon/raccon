package com.sparta.spartaproject.domain.pay;

import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.OrderRepository;
import com.sparta.spartaproject.domain.order.PayMethod;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.store.StoreRepository;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreatePayHistoryRequestDto;
import com.sparta.spartaproject.dto.request.UpdatePayHistoryDto;
import com.sparta.spartaproject.dto.response.OnlyPayHistoryDto;
import com.sparta.spartaproject.dto.response.PayHistoryDetailDto;
import com.sparta.spartaproject.dto.response.PayHistoryDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.mapper.PayHistoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.sparta.spartaproject.domain.order.OrderStatus.CANCEL;
import static com.sparta.spartaproject.domain.pay.PayStatus.CANCELLED;
import static com.sparta.spartaproject.domain.pay.PayStatus.COMPLETED;
import static com.sparta.spartaproject.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayHistoryService {

    private final PayHistoryRepository payHistoryRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final PayHistoryMapper payHistoryMapper;
    private final UserService userService;

    private final Integer size = 10;

    @Transactional
    public void createPayHistory(CreatePayHistoryRequestDto request) {
        User user = getUser();
        Order order = orderRepository.findByIdAndUserAndIsDeletedFalse(request.orderId(), user)
                .orElseThrow(() -> new BusinessException(ORDER_NOT_EXIST));

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new BusinessException(STORE_NOT_FOUND));

//        PayHistory payHistory = payHistoryRepository.findByOrderId(request.orderId());
//        PayHistory payHistory = payHistoryMapper.toPayHistory(request, order, store, user);


//        payHistory.updateStatus(COMPLETED);
//        payHistoryRepository.save(payHistory);
    }

    @Transactional(readOnly = true)
    public PayHistoryDetailDto getPayHistoryDetail(UUID payHistoryId) {
        PayHistory payHistory = payHistoryRepository.findById(payHistoryId)
                .orElseThrow(() -> new BusinessException(PAY_HISTORY_NOT_EXIST));

        return payHistoryMapper.toPayHistoryDetailDto(payHistory, payHistory.getStatus().description, payHistory.getPayMethod().getDescription());
    }

    @Transactional
    public void updatePayHistory(UUID payHistoryId, UpdatePayHistoryDto update) {
        PayHistory payHistory = payHistoryRepository.findById(payHistoryId)
                .orElseThrow(() -> new BusinessException(PAY_HISTORY_NOT_EXIST));

        payHistory.updatePayHistory(update);

        payHistoryRepository.save(payHistory);
    }

    public void deletePayHistory(UUID payHistoryId) {
        PayHistory payHistory = payHistoryRepository.findById(payHistoryId)
                .orElseThrow(() -> new BusinessException(PAY_HISTORY_NOT_EXIST));

        payHistory.getOrder().updateStatus(CANCEL);

        payHistory.updateStatus(CANCELLED);

        payHistory.deletePayHistory();

        payHistoryRepository.save(payHistory);
    }

    public PayHistoryDto getPayHistoryList(int page) {
        Pageable pageable = PageRequest.of(page - 1, size);

        User user = getUser();

        List<PayHistory> payHistoryList = payHistoryRepository.findAllByUser(pageable, user);

        List<OnlyPayHistoryDto> onlyPayHistoryDtoList = payHistoryList.stream()
                .map(
                        payHistory ->
                                payHistoryMapper.toOnlyPayHistoryDto(payHistory,payHistory.getStatus().description, payHistory.getPayMethod().getDescription())
                ).toList();

        int totalCount = payHistoryList.size();

        return payHistoryMapper.toPayHistoryDto(
                onlyPayHistoryDtoList
                , page
                , (int) Math.ceil((double) totalCount / size)
                , totalCount
        );
    }


    private User getUser() {
        return userService.loginUser();
    }


    public void savePayHistory(Order order, Store store, User user, PayMethod payMethod) {
        PayHistory newPayHistory = payHistoryMapper.toPayHistory(order, store, user, payMethod);
        payHistoryRepository.save(newPayHistory);

        log.info("결제 내역 저장");
    }
}
