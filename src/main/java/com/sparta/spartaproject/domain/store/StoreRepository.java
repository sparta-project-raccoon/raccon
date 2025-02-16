package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.category.Category;
import com.sparta.spartaproject.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    Page<Store> findByCategory(Category category, Pageable pageable);

    Page<Store> findByOwner(User owner, Pageable pageable);

    Page<Store> findByNameContainingOrDescriptionContaining(String searchWord1, String searchWord2, Pageable pageable);

}
