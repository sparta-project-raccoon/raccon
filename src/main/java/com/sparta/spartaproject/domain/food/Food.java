package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.domain.BaseEntity;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.food.Status;
import com.sparta.spartaproject.dto.request.UpdateFoodRequestDto;
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

    @Id //식별자 필드로 엔티티의 필드를 테이블의 기본 키로 매핑
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Column(
            length = 30
    )
    private String name;

    private Integer price;

    @Column(
            length = 300
    )
    private String description;

    @Column(columnDefinition = "TEXT")
    private String  imagePath;

    //잘 모름
    @Enumerated(EnumType.STRING)
    private Status status;

    //삭제여부
    @Column(
            nullable = false,
            columnDefinition = "boolean default false"
    )
    private Boolean isDeleted;

    //삭제일시
    private LocalDateTime deletedAt;


    public void update(UpdateFoodRequestDto update) {
        this.name = update.name();
        this.price = update.price();
        this.description = update.description();
        this.status = update.status();
    }

    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    public void updateImagePath(String newImagePath) {
        this.imagePath = newImagePath;
    }

    public void delete(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

}