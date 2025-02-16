package com.sparta.spartaproject.domain.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findAllByUserIdAndIsDeletedIsFalse(Pageable pageable, Long userId);

    Optional<Review> findByIdAndIsDeletedIsFalse(UUID id);

    List<Review> findAllByStoreIdAndIsDeletedIsFalse(Pageable pageable, UUID storeId);
}