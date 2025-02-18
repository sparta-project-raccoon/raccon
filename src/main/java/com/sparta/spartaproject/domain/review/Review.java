package com.sparta.spartaproject.domain.review;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.order.Order;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.UpdateReviewRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Table(name = "p_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(
        length = 200
    )
    private String content;

    @Column(
        nullable = false,
        columnDefinition = "int default 0"
    )
    private Integer rating;

    @Column(
        nullable = false,
        columnDefinition = "boolean default false"
    )
    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    public void update(UpdateReviewRequestDto update) {
        if (update.content() != null) {
            this.content = update.content();
        }

        if (update.rating() != null) {
            this.rating = update.rating();
        }
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}