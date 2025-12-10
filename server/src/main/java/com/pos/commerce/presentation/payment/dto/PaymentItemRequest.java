package com.pos.commerce.presentation.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentItemRequest {
    /* @결제 아이템 상품 아이디 */  
    private Long productId;
    /* @결제 아이템 상품 이름 */
    private String productName;
    /* @결제 아이템 수량 */
    private Integer quantity;
    /* @결제 아이템 단가 */
    private BigDecimal unitPrice;
}




