package com.sparta.spartaproject.domain;

import com.sparta.spartaproject.domain.food.FoodService;
import com.sparta.spartaproject.domain.gemini.GeminiService;
import com.sparta.spartaproject.domain.mail.MailService;
import com.sparta.spartaproject.domain.order.OrderHistoryService;
import com.sparta.spartaproject.domain.order.OrderService;
import com.sparta.spartaproject.domain.pay.PayHistoryService;
import com.sparta.spartaproject.domain.review.ReviewService;
import com.sparta.spartaproject.domain.store.StoreCategoryService;
import com.sparta.spartaproject.domain.store.StoreService;
import com.sparta.spartaproject.domain.user.CustomUserDetailsService;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.domain.verify.VerifyService;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CircularService {
    private final UserService userService;
    private final MailService mailService;
    private final FoodService foodService;
    private final StoreService storeService;
    private final OrderService orderService;
    private final VerifyService verifyService;
    private final GeminiService geminiService;
    private final ReviewService reviewService;
    private final PayHistoryService payHistoryService;
    private final OrderHistoryService orderHistoryService;
    private final StoreCategoryService storeCategoryService;
    private final CustomUserDetailsService customUserDetailsService;

    public CircularService(
        @Lazy UserService userService,
        @Lazy MailService mailService,
        @Lazy FoodService foodService,
        @Lazy StoreService storeService,
        @Lazy OrderService orderService,
        @Lazy VerifyService verifyService,
        @Lazy GeminiService geminiService,
        @Lazy ReviewService reviewService,
        @Lazy PayHistoryService payHistoryService,
        @Lazy OrderHistoryService orderHistoryService,
        @Lazy StoreCategoryService storeCategoryService,
        @Lazy CustomUserDetailsService customUserDetailsService
    ) {
        this.userService = userService;
        this.mailService = mailService;
        this.foodService = foodService;
        this.storeService = storeService;
        this.orderService = orderService;
        this.verifyService = verifyService;
        this.geminiService = geminiService;
        this.reviewService = reviewService;
        this.payHistoryService = payHistoryService;
        this.orderHistoryService = orderHistoryService;
        this.storeCategoryService = storeCategoryService;
        this.customUserDetailsService = customUserDetailsService;
    }
}