package com.pos.commerce.application.payment.command;

import java.math.BigDecimal;
import java.util.List;

import com.pos.commerce.domain.payment.PaymentMethod;

public record CreatePaymentCommand(
        BigDecimal totalAmount,
        PaymentMethod method,
        String userId,
        List<PaymentItemCommand> items
) {

    public record PaymentItemCommand(
            Long productId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice
    ) {
    }
}





