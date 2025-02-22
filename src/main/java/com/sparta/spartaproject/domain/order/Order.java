package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Store store;

    @Column(
        nullable = false
    )
    private Integer totalPrice;

    private String request;

    @Column(
        nullable = false
    )
    @Enumerated(EnumType.STRING)
    private OrderMethod orderMethod;

    @Column(
        nullable = false
    )
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Column(
        nullable = false
    )
    private String address;

    @Column(
        nullable = false
    )
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Integer totalFoodCount;

    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "order")
    private List<OrderHistory> orderHistories = new ArrayList<>();

    public void updateStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public void updateTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void deleteOrder() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}