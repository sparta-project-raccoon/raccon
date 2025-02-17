package com.sparta.spartaproject.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, UUID> {
    List<Like> findAllByUserIdAndIsDeletedIsFalse(Long userId);

    Optional<Like> findByIdAndIsDeletedIsFalse(UUID id);

    Optional<Like> findByUserIdAndStoreIdAndIsDeletedIsFalse(Long userId, UUID storeId);
}