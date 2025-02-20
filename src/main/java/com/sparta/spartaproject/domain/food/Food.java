package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
import com.sparta.spartaproject.dto.request.UpdateFoodStatusRequestDto;
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
@Table(name = "p_food")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Column(
        length = 30,
        nullable = false
    )
    private String name;

    @Column(
        nullable = false
    )
    private Integer price;

    @Column(
        length = 300
    )
    private String description;

    @Column(
        columnDefinition = "TEXT"
    )
    private String imagePath;

    @Column(
        nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Boolean isDisplayed = true;

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

    public void update(UpdateFoodRequestDto update) {
        if (update.name() != null) {
            this.name = update.name();
        }

        if (update.price() != null) {
            this.price = update.price();
        }

        if (update.description() != null) {
            this.description = update.description();
        }

        if (update.status() != null) {
            this.status = update.status();
        }
    }

    public void updateStatus(UpdateFoodStatusRequestDto update) {
        this.status = update.status();
    }

    public void toggleIsDisplayed() {
        this.isDisplayed = !this.isDisplayed;
    }

    public void updateImagePath(String newImagePath) {
        this.imagePath = newImagePath;
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}