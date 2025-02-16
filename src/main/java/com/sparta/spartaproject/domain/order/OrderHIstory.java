package com.sparta.spartaproject.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Table(name = "orders_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHIstory {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // todo 음식 추가
    private UUID food_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order_id;

    private BigDecimal price; // 음식 단일 가격
    private Integer qty; // 수량
}
