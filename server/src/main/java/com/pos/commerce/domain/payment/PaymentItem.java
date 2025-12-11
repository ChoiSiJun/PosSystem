package com.pos.commerce.domain.payment;

import com.pos.commerce.infrastructure.config.BigDecimalEncryptionConverter;
import com.pos.commerce.infrastructure.config.IntegerEncryptionConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentItem {

    /* @결제 아이템 아이디 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* @결제 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    /* @결제 아이템 상품 아이디 */
    @Column(nullable = false)
    private Long productId;

    /* @결제 아이템 상품 이름 */
    @Column(nullable = false)
    private String productName;

    /* @결제 아이템 수량 */
    @Column(nullable = false)
    @Convert(converter = IntegerEncryptionConverter.class)
    private Integer quantity;

    /* @결제 아이템 단가 */
    @Column(nullable = false)
    @Convert(converter = BigDecimalEncryptionConverter.class)
    private BigDecimal unitPrice;

    /* @결제 아이템 총 금액 */
    @Column(nullable = false)
    @Convert(converter = BigDecimalEncryptionConverter.class)
    private BigDecimal totalPrice;

    /* @결제 아이템 총 금액 계산 */
    public BigDecimal calculateTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

