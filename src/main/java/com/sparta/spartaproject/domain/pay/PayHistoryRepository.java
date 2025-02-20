package com.sparta.spartaproject.domain.pay;

import com.sparta.spartaproject.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PayHistoryRepository extends JpaRepository<PayHistory, UUID> {

    List<PayHistory> findAllByUser(Pageable pageable, User user);
}
