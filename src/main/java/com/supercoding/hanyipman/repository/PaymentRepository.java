package com.supercoding.hanyipman.repository;

import com.supercoding.hanyipman.entity.Order;
import com.supercoding.hanyipman.entity.OrderTest;
import com.supercoding.hanyipman.entity.Payment;
import com.supercoding.hanyipman.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Payment findPaymentByImpUidAndMerchantUid (String impUid, String merchantUid);
    Optional<Payment> findPaymentByOrderTestId (OrderTest orderTest);
    Payment findPaymentByOrderTestIdId (Long orderId);

}
