package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.orderStatus != :status")
    Optional<Order> findByIdStatusNot(UUID orderId, @Param("status") OrderStatus orderStatus);

    @Query("SELECT o FROM Order o where o.orderStatus != :status and o.user = :user")
    List<Order> findAllByUserStatusNot(@Param("user") User user, @Param("status") OrderStatus orderStatus);

}
