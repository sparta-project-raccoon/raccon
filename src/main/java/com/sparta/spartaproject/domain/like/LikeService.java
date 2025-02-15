package com.sparta.spartaproject.domain.like;

import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.domain.user.UserService;
import com.sparta.spartaproject.dto.request.CreateLikeRequestDto;
import com.sparta.spartaproject.dto.response.LikeDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.spartaproject.exception.ErrorCode.LIKE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserService userService;
    private final LikeMapper likeMapper;
    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public List<LikeDto> getLikes() {
        User user = userService.loginUser();

//        if (user.getRole() != Role.CUSTOMER) {
//            //  클라이언트가 요청을 보냈으나, 권한이 없어서 접근을 거부하는 경우
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다.");
//        }

        return likeRepository.findAllByUserIdAndIsDeletedIsFalse(user.getId()).stream().map(
            likeMapper::toLikeDto
        ).toList();
    }

    @Transactional(readOnly = true)
    public LikeDto getLike(UUID id) {
        Like like = getLikeByIdAndIsDeletedIsFalse(id);

        User user = userService.loginUser();
        // TODO: Store 검증 코드

        if (!Objects.equals(like.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다.");
        }

        return likeMapper.toLikeDto(like);
    }

    @Transactional
    public void toggleLike(CreateLikeRequestDto request) {
        User user = userService.loginUser();
        log.info("{}", user.getId());

        // Store store = storeService.getStoreById(request.storeId());
        Optional<Like> like = likeRepository.findByUserIdAndStoreIdAndIsDeletedIsFalse(user.getId(), request.storeId());

        if (like.isPresent()) {
            // 이미 찜한 경우
            likeRepository.delete(like.get());
            log.info("찜 취소");
        } else {
            Like newLike = likeMapper.toLike(request, user);
            likeRepository.save(newLike);
            log.info("찜 등록");
        }
    }

    @Transactional
    public void deleteLike(UUID id) {
        Like like = likeRepository.findById(id)
            .orElseThrow(() -> new BusinessException(LIKE_NOT_FOUND));

        like.delete();
        log.info("찜:{}, 삭제", id);
    }


    public Like getLikeByIdAndIsDeletedIsFalse(UUID id) {
        // TODO: 카테고리 에러 메시지 적용, 404
        return likeRepository.findByIdAndIsDeletedIsFalse(id)
            .orElseThrow(() -> new BusinessException(LIKE_NOT_FOUND));
    }
}