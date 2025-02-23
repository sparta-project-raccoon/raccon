package com.sparta.spartaproject.domain.pay;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PayHistoryRepository extends JpaRepository<PayHistory, UUID> {
    @Query(
        "SELECT count(ph) " +
            "FROM PayHistory ph"
    )
    Long countPayHistory();

    @Query(
        "SELECT DISTINCT ph " +
            "FROM PayHistory ph " +
            "LEFT JOIN FETCH ph.order o " +
            "LEFT JOIN FETCH ph.store s"
    )
    Page<PayHistory> findPayHistoryList(Pageable pageable);

    @Query(
        "SELECT count(ph) " +
            "FROM PayHistory ph " +
            "WHERE ph.user.id = :userId"
    )
    Long countPayHistoryByUserId(@Param("userId") Long userId);

    @Query(
        "SELECT DISTINCT ph " +
            "FROM PayHistory ph " +
            "LEFT JOIN FETCH ph.order o " +
            "LEFT JOIN FETCH ph.store s " +
            "WHERE ph.user.id = :userId"
    )
    Page<PayHistory> findPayHistoryListByUserId(Pageable pageable, @Param("userId") Long userId);

    @Query(
        "SELECT count(ph) " +
            "FROM PayHistory ph " +
            "WHERE ph.store.id = :storeId"
    )
    Long countPayHistoryByStoreId(@Param("storeId") UUID storeId);

    @Query(
        "SELECT DISTINCT ph " +
            "FROM PayHistory ph " +
            "LEFT JOIN FETCH ph.order o " +
            "LEFT JOIN FETCH ph.store s " +
            "WHERE ph.store.id = :storeId"
    )
    Page<PayHistory> findPayHistoryListByStoreId(Pageable pageable, @Param("storeId") UUID storeId);
}