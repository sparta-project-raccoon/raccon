package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.domain.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {
    Optional<Food> findByIdAndIsDeletedFalse(UUID id);

    Page<Food> findByStoreAndIsDisplayedIsTrueAndIsDeletedIsFalse(Store store, Pageable pageable);

    Page<Food> findByStoreAndIsDeletedIsFalse(Store store, Pageable pageable);
}