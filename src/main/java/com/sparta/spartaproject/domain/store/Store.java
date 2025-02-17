package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Table(name = "p_stores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "category_id", nullable = false)
//    private Category category;

    @Column(
        nullable = false,
        length = 50
    )
    private String name;

    @Column(
        nullable = false,
        length = 300
    )
    private String address;

    @Column(
        nullable = false,
        length = 20
    )
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(
        nullable = false,
        length = 20
    )
    private String tel;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ClosedDays closedDays;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();  // UUID 자동 생성
        }
    }

    public void update(UpdateStoreRequestDto update, Category category) {
//        if (category != null) {
//            this.category = category;
//        }

        if (update.address() != null) {
            this.address = update.address();
        }

        if (update.status() != null) {
            this.status = update.status();
        }

        if (update.tel() != null) {
            this.tel = update.tel();
        }

        if (update.description() != null) {
            this.description = update.description();
        }

        if (update.openTime() != null) {
            this.openTime = update.openTime();
        }

        if (update.closeTime() != null) {
            this.closeTime = update.closeTime();
        }

        if (update.closedDays() != null) {
            this.closedDays = update.closedDays();
        }
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
