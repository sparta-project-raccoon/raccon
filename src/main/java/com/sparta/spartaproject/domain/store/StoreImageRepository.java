package com.sparta.spartaproject.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreImageRepository extends JpaRepository<StoreImage, UUID> {
    List<StoreImage> findByStoreAndIsDeleteIsFalseOrderByCreatedAtAsc(Store store);
}
