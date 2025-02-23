package com.sparta.spartaproject.domain.food;

import com.sparta.spartaproject.domain.store.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {
    Optional<Food> findByIdAndIsDeletedFalse(UUID id);

    List<Food> findByStoreAndIsDisplayedIsTrueAndIsDeletedIsFalse(Store store, Pageable pageable);

    List<Food> findByStoreAndIsDeletedIsFalse(Store store, Pageable pageable);
}