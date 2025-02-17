package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.category.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
    @Query(
        "SELECT sc.category " +
            "FROM StoreCategory sc " +
            "WHERE sc.isDeleted IS FALSE " +
            "AND sc.store = :store"
    )
    List<Category> findAllCategoryListByStore(
        @Param("store") Store store
    );

    @Query(
        "SELECT sc.store " +
            "FROM StoreCategory sc " +
            "WHERE sc.isDeleted IS FALSE " +
            "AND sc.category = :category"
    )
    List<Store> findAllStoreListByCategory(
        Pageable pageable,
        @Param("category") Category category
    );
}