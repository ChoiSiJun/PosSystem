package com.pos.commerce.infrastructure.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.commerce.domain.payment.Payment;
import com.pos.commerce.domain.payment.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentNumber(String paymentNumber);
    
    /* @날짜 범위로 결제 내역 조회 */
    List<Payment> findByShopCodeAndStatusAndCreatedAtBetween(String shopCode, PaymentStatus status, LocalDateTime startDate, LocalDateTime endDate);
    
    /* @매장 코드와 상태로 결제 조회 */
    List<Payment> findByShopCodeAndStatus(String shopCode, PaymentStatus status);
}




