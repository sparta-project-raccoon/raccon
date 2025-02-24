package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.food.Food;
import com.sparta.spartaproject.domain.image.Image;
import com.sparta.spartaproject.domain.review.Review;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.UpdateStoreRequestDto;
import com.sparta.spartaproject.dto.request.UpdateStoreStatusRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    private String description;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ClosedDays closedDays;

    @Column(
        nullable = false,
        columnDefinition = "boolean default false"
    )
    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    @Column(
        nullable = false,
        columnDefinition = "bool default false"
    )
    private Boolean isConfirmed;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreCategory> storeCategories = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "entityId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();  // UUID 자동 생성
        }
    }

    public void update(UpdateStoreRequestDto update) {
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
            this.openTime = LocalTime.parse(update.openTime(), DateTimeFormatter.ofPattern("HH:mm"));
        }

        if (update.closeTime() != null) {
            this.closeTime = LocalTime.parse(update.closeTime(), DateTimeFormatter.ofPattern("HH:mm"));
        }

        if (update.closedDays() != null) {
            this.closedDays = update.closedDays();
        }
    }

    public void updateStatus(UpdateStoreStatusRequestDto update) {
        this.status = update.status();
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void confirm() {
        this.isConfirmed = true;
    }
}