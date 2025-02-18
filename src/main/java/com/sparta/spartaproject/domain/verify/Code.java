package com.sparta.spartaproject.domain.verify;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Code {
    private final String codeNumber;
    private final LocalDateTime issuedAt;
    private int errorCount = 0;

    public void incrementCount() {
        this.errorCount++;
    }
}