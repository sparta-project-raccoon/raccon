package com.sparta.spartaproject.controller;

import com.sparta.spartaproject.domain.like.LikeService;
import com.sparta.spartaproject.dto.request.CreateLikeRequestDto;
import com.sparta.spartaproject.dto.response.LikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {
    private final LikeService likeService;

    @Description(
        "사용자용 - 찜 전체 조회"
    )
    @GetMapping("/likes")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MASTER', 'MANAGER')")
    public ResponseEntity<List<LikeDto>> getLikes() {
        return ResponseEntity.ok(likeService.getLikes());
    }

    @Description(
        "찜 상세 조회"
    )
    @GetMapping("/likes/{id}")
    public ResponseEntity<LikeDto> getLike(@PathVariable UUID id) {
        return ResponseEntity.ok(likeService.getLike(id));
    }

    @Description(
        "찜 하기 - 토글 방식"
    )
    @PostMapping("/likes")
    public ResponseEntity<Void> toggleLike(@RequestBody CreateLikeRequestDto request) {
        likeService.toggleLike(request);
        return ResponseEntity.ok().build();
    }

    @Description(
        "찜 삭제"
    )
    @DeleteMapping("/likes/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MASTER', 'MANAGER')")
    public ResponseEntity<Void> deleteLike(@PathVariable UUID id) {
        likeService.deleteLike(id);
        return ResponseEntity.ok().build();
    }
}