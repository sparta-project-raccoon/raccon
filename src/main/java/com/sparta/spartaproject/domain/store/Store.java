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
    private UUID id;  // 음식점 ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;  // 가게 주인 (User 테이블과 연결)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;  // 카테고리 (Category 테이블과 연결)

    @Column(
        nullable = false,
        length = 50
    )
    private String name;  // 음식점 이름

    @Column(
        nullable = false,
        length = 300
    )
    private String address;  // 음식점 주소

    @Column(
        nullable = false,
        length = 20
    )
    @Enumerated(EnumType.STRING)
    private Status status;  // BEFORE_OPEN, OPEN, CLOSE, BREAK_TIME

    @Column(
        nullable = false,
        length = 20
    )
    private String tel;  // 전화번호

    @Column(length = 255)
    private String description;  // 음식점 상세 소개

    @Column(nullable = false)
    private LocalTime openTime;  // 오픈 시간

    @Column(nullable = false)
    private LocalTime closeTime;  // 마감 시간

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private ClosedDays closedDays; // 휴무일


    public void update(UpdateStoreRequestDto update, Category category) {
        this.category = category;
        this.name = update.name();
        this.address = update.address();
        this.status = update.status();
        this.tel = update.tel();
        this.description = update.description();
        this.openTime = update.openTime();
        this.closeTime = update.closeTime();
        this.closedDays = update.closedDays();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();  // UUID 자동 생성
        }
    }

}
