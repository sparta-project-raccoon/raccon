package com.sparta.spartaproject.domain.user;

import com.sparta.spartaproject.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        nullable = false,
        length = 10
    )
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(
        nullable = false,
        length = 30
    )
    private String email;

    @Column(
        nullable = false,
        length = 30
    )
    private String name;

    @Column(
        nullable = false,
        length = 30
    )
    private String phone;

    @Column(
        nullable = false,
        length = 300
    )
    private String address;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    private Integer loginFailCount;

    private LocalDateTime passwordChangedAt;

    public void failLogin() {
        this.loginFailCount++;
    }

    public void updateStatusStopped() {
        this.status = Status.STOPPED;
    }

    public void successLogin() {
        this.loginFailCount = 0;
    }

    public void updatePassword(String password) {
        this.password = password;
        this.passwordChangedAt = LocalDateTime.now();
    }
}