package com.sparta.spartaproject.domain.image.store;

import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StoreImageRepository extends JpaRepository<StoreImage, UUID> {
    List<StoreImage> findByStoreOrderByCreatedAtAsc(Store store);
}
