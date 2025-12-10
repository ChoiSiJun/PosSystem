package com.pos.commerce.application.payment.command;

import java.math.BigDecimal;
import java.util.List;

import com.pos.commerce.domain.payment.PaymentMethod;

public record CreatePaymentCommand(
        /* @결제 총 금액 */
        BigDecimal totalAmount,
        /* @결제 방법 */
        PaymentMethod method,
        /* @결제 아이템 */
        List<PaymentItemCommand> items
) {

    /* @결제 아이템 */
    public record PaymentItemCommand(
            /* @결제 아이템 상품 아이디 */
            Long productId,
            /* @결제 아이템 상품 이름 */
            String productName,
            /* @결제 아이템 수량 */
            Integer quantity,
            /* @결제 아이템 단가 */
            BigDecimal unitPrice
    ) {
    }
}





