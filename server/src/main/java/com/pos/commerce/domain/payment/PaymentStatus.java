package com.pos.commerce.domain.payment;

public enum PaymentStatus {
    /* @대기 */ 
    PENDING,
    /* @완료 */
    COMPLETED,
    /* @취소 */
    CANCELLED,
    /* @실패 */
    FAILED
}

