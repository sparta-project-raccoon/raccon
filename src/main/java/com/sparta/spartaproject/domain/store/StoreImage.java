package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.BaseTimeEntity;
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
@Table(name = "p_store_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreImage extends BaseTimeEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(
            length = 500,
            nullable = false
    )
    private String path;

    @Column(
            nullable = false
    )
    private Boolean isDeleted = false;

    private LocalDateTime deleteAt;

    // 삭제 여부 값 변경 및 삭제 일시 생성
    public void delete() {
        isDeleted = true;
        deleteAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();  // UUID 자동 생성
        }
    }
}
