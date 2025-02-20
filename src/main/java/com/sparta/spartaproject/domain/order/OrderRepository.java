package com.sparta.spartaproject.domain.order;

import com.sparta.spartaproject.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByUserAndIsDeletedFalse(Pageable pageable, User user);

    Optional<Order> findByIdAndUserAndIsDeletedFalse(UUID orderId, User user);

    Optional<Order> findByIdAndStoreOwnerAndIsDeletedIsFalse(UUID orderId, User user);
}