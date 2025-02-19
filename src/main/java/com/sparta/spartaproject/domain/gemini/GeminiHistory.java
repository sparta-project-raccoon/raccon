package com.sparta.spartaproject.domain.gemini;

import com.sparta.spartaproject.domain.BaseTimeEntity;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.UpdateGeminiHistoryRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Getter
@Entity
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@Table(name = "p_gemini_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeminiHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @Column(
        length = 50,
        nullable = false
    )
    private String requestText;

    @Column(
        length = 100
    )
    private String responseText;

    @Column(
        nullable = false
    )
    private Integer statusCode;

    public void update(UpdateGeminiHistoryRequestDto update, Store store) {
        if (this.store != store) {
            this.store = store;
        }

        if (update.requestText() != null) {
            this.requestText = update.requestText();
        }

        if (update.responseText() != null) {
            this.responseText = update.responseText();
        }

        if (update.statusCode() != null) {
            this.statusCode = update.statusCode();
        }
    }
}