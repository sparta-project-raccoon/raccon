package com.sparta.spartaproject.domain.pay;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.PaymentMethod;
import com.sparta.spartaproject.domain.user.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "p_pay_history")
public class PayHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentMethod payMethod;
}
