package com.sparta.spartaproject.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    List<Image> findByEntityIdAndEntityType(UUID entityId, EntityType entityType);
}