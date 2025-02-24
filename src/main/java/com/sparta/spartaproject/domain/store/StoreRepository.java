package com.sparta.spartaproject.domain.store;

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
}