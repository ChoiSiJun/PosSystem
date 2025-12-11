package com.pos.commerce.presentation.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

import com.pos.commerce.domain.payment.PaymentMethod;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    /* @결제 총 금액 */
    private BigDecimal totalAmount;
    /* @결제 방법 */
    private PaymentMethod method;
    /* @결제 아이템 */
    private List<PaymentItemRequest> items;
}




