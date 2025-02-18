package com.sparta.spartaproject.domain.like;

import com.sparta.spartaproject.domain.CircularService;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateLikeRequestDto;
import com.sparta.spartaproject.dto.response.LikeDto;
import com.sparta.spartaproject.exception.BusinessException;
import com.sparta.spartaproject.exception.ErrorCode;
import com.sparta.spartaproject.mapper.LikeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.spartaproject.exception.ErrorCode.LIKE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeMapper likeMapper;
    private final LikeRepository likeRepository;
    private final CircularService circularService;

    @Transactional(readOnly = true)
    public List<LikeDto> getLikes() {
        User user = circularService.getUserService().loginUser();

        return likeRepository.findAllByUserIdAndIsDeletedIsFalse(user.getId()).stream().map(
            likeMapper::toLikeDto
        ).toList();
    }

    @Transactional(readOnly = true)
    public LikeDto getLike(UUID id) {
        User user = circularService.getUserService().loginUser();
        Like like = getLikeByIdAndIsDeletedIsFalse(id);

        if (!Objects.equals(like.getUser().getId(), user.getId())) {
            throw new BusinessException(ErrorCode.LIKE_FORBIDDEN);
        }

        return likeMapper.toLikeDto(like);
    }

    @Transactional
    public void toggleLike(CreateLikeRequestDto request) {
        User user = circularService.getUserService().loginUser();
        Store store = circularService.getStoreService().getStoreByIdAndIsDeletedIsFalse(request.storeId());
        Optional<Like> like = likeRepository.findByUserIdAndStoreIdAndIsDeletedIsFalse(user.getId(), store.getId());

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            log.info("찜 취소");
        } else {
            Like newLike = likeMapper.toLike(store, user);
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
        return likeRepository.findByIdAndIsDeletedIsFalse(id)
            .orElseThrow(() -> new BusinessException(LIKE_NOT_FOUND));
    }
}