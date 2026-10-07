package com.example.msa_db_sep_hongkd.ordering.repository;

import com.example.msa_db_sep_hongkd.ordering.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByMemberIdOrderByCreatedTimeDesc(long memberId);
}
