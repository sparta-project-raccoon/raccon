package com.sparta.spartaproject.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.orderStatus != :status")
    Optional<Order> findByIdStatusNot(UUID orderId, @Param("status") OrderStatus orderStatus);
}
