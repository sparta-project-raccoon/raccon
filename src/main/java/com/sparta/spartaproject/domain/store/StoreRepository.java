package com.sparta.spartaproject.domain.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    @Query(
        "SELECT s " +
            "FROM Store s " +
            "WHERE s.isDeleted IS FALSE " +
            "AND s.name LIKE CONCAT('%', :name, '%')"
    )
    List<Store> findAllStoreList(
        Pageable pageable,
        @Param("name") String name
    );

    @Query(
        "SELECT s " +
            "FROM Store s " +
            "WHERE s.isDeleted IS FALSE " +
            "AND s.owner.id = :ownerId"
    )
    List<Store> findAllStoreListByOwner(
        Pageable pageable,
        @Param("ownerId") Long ownerId
    );

    Optional<Store> findByIdAndIsDeletedIsFalse(UUID id);

    Optional<Store> findByOwnerIdAndIsDeletedIsFalse(Long ownerId);

    @Query(
        "SELECT count(DISTINCT s) " +
            "FROM Store  s " +
            "WHERE s.isConfirmed IS FALSE"
    )
    Long countUnconfirmed();

    @Query(
        "SELECT DISTINCT s " +
            "FROM Store  s " +
            "LEFT JOIN FETCH s.storeCategories sc " +
            "LEFT JOIN FETCH sc.category c " +
            "WHERE s.isConfirmed IS FALSE"
    )
    Page<Store> findAllByUnConfirmed(Pageable pageable);
}