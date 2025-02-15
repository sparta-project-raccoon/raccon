package com.sparta.spartaproject.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);

    Optional<Category> findById(UUID id);
}