package com.pos.commerce.infrastructure.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.commerce.domain.payment.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentNumber(String paymentNumber);
    
    /* @날짜 범위로 결제 내역 조회 */
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}




