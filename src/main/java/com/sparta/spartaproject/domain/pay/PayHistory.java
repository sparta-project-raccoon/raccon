package com.sparta.spartaproject.domain.pay;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.order.PaymentMethod;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.UpdatePayHistoryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_pay_history")
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void updatePayHistory(UpdatePayHistoryDto update) {
        if(update.paymentMethod() != null){
            this.paymentMethod = update.paymentMethod();
        }

        if(update.totalPrice() != null){
            this.order.updateTotalPrice(update.totalPrice());
        }
    }

    public void deletePayHistory() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}
