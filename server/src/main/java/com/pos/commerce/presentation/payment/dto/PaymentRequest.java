package com.pos.commerce.presentation.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.pos.commerce.domain.payment.PaymentMethod;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private BigDecimal totalAmount;
    private PaymentMethod method;
    private String userId;
    private List<PaymentItemRequest> items;
}




