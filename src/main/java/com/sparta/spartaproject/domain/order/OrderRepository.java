package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.status != :status and o.id = :id")
    Optional<Order> findById(@Param("id") UUID id, @Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o where o.status != :status and o.user = :user")
    List<Order> findAllByUser(Pageable pageable, @Param("user") User user, @Param("status") OrderStatus status);

}
