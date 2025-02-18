package com.sparta.spartaproject.domain.food;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID> {
}
