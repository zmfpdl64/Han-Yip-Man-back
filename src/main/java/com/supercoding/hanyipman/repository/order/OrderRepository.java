package com.supercoding.hanyipman.repository.order;

import com.supercoding.hanyipman.entity.Order;
import com.supercoding.hanyipman.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long orderId);
    // 업주 찾기
    @Query("SELECT s FROM Shop p JOIN p.seller s WHERE p.id = :shopId")
    Optional<Seller> findSellerByShopId(@Param("shopId") Long shopId);
}