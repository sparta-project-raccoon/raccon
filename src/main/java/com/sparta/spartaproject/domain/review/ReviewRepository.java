package com.sparta.spartaproject.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findAllByUserIdAndIsDeletedIsFalse(Pageable pageable, Long userId);

    Page<Review> findAllByIsDeletedIsFalse(Pageable pageable);

    Optional<Review> findByIdAndIsDeletedIsFalse(UUID id);

    Page<Review> findAllByStoreIdAndIsDeletedIsFalse(Pageable pageable, UUID storeId);

    Boolean existsByStoreIdAndOrderIdAndIsDeletedIsFalse(UUID storeId, UUID orderId);

    Long countByStoreIdAndIsDeletedIsFalse(UUID storeId);
}