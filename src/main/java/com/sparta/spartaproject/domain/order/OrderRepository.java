package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByIdAndUserAndIsDeletedFalse(UUID orderId, User user);

    Optional<Order> findByIdAndIsDeletedIsFalse(UUID orderId);

    @Query(
        "SELECT COUNT(DISTINCT o) " +
            "FROM Order o"
    )
    Long countByOrder();

    @Query(
        "SELECT o " +
            "FROM Order o " +
            "LEFT JOIN FETCH o.orderHistories oh " +
            "LEFT JOIN fetch oh.food "
    )
    Page<Order> findAllWithOrderHistoriesAndFoods(Pageable pageable);

    @Query(
        "SELECT count(DISTINCT o) " +
            "FROM Order o " +
            "WHERE o.store.id = :storeId"
    )
    Long countAllByStoreId(@Param("storeId") UUID storeId);

    @Query(
        "SELECT DISTINCT o " +
            "FROM Order o " +
            "LEFT JOIN FETCH o.orderHistories oh " +
            "LEFT JOIN FETCH oh.food " +
            "WHERE o.store.id = :storeId"
    )
    Page<Order> findAllWithOrderHistoriesAndFoodsByStoreId(@Param("storeId") UUID storeId, Pageable pageable);

    @Query(
        "SELECT count(DISTINCT o) " +
            "FROM Order o " +
            "WHERE o.id = :orderId"
    )
    Long countAllByOrderId(@Param("orderId") UUID orderId);

    @Query(
        "SELECT DISTINCT o " +
            "FROM Order o " +
            "LEFT JOIN FETCH o.orderHistories oh " +
            "LEFT JOIN FETCH oh.food " +
            "WHERE o.id = :orderId"
    )
    Order findByOrderId(@Param("orderId") UUID orderId);
}