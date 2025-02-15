package com.sparta.spartaproject.domain.category;

import com.sparta.spartaproject.domain.BaseTimeEntity;
import com.sparta.spartaproject.dto.request.UpdateCategoryRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        length = 30,
        unique = true,
        nullable = false
    )
    private String name;

    public void update(UpdateCategoryRequestDto update) {
        this.name = update.name();
    }
}