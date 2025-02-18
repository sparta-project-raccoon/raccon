package com.sparta.spartaproject.domain;

import com.sparta.spartaproject.domain.mail.MailService;
import com.sparta.spartaproject.domain.order.OrderService;
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
    private final StoreService storeService;
    private final OrderService orderService;
    private final VerifyService verifyService;
    private final CustomUserDetailsService customUserDetailsService;

    public CircularService(
        @Lazy UserService userService,
        @Lazy MailService mailService,
        @Lazy StoreService storeService,
        @Lazy OrderService orderService,
        @Lazy VerifyService verifyService,
        @Lazy CustomUserDetailsService customUserDetailsService
    ) {
        this.userService = userService;
        this.mailService = mailService;
        this.storeService = storeService;
        this.orderService = orderService;
        this.verifyService = verifyService;
        this.customUserDetailsService = customUserDetailsService;
    }
}