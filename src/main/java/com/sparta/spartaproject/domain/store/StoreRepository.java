package com.sparta.spartaproject.domain.store;

import com.sparta.spartaproject.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
            "AND s.owner = :owner"
    )
    List<Store> findAllStoreListByOwner(
        @Param("owner") User owner
    );
}