package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.BaseTimeEntity;
import com.sparta.spartaproject.domain.store.Store;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;
@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Table(name = "p_order_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // todo 음식 추가
    private UUID food_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    private int price; // 음식 단일 가격
    private Integer qty; // 수량
}
