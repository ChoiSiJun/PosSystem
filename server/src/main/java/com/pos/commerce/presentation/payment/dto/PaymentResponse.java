package com.pos.commerce.presentation.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.pos.commerce.domain.payment.Payment;
import com.pos.commerce.domain.payment.PaymentMethod;
import com.pos.commerce.domain.payment.PaymentStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private String paymentNumber;
    private BigDecimal totalAmount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String userId;
    private List<PaymentItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentNumber(payment.getPaymentNumber())
                .totalAmount(payment.getTotalAmount())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .userId(payment.getUserId())
                .items(payment.getItems().stream()
                        .map(PaymentItemResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}

