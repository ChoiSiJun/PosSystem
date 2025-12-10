package com.pos.commerce.application.payment.query;

import java.time.LocalDateTime;

public record GetPaymentsByDateRangeQuery(
        /* @시작 날짜 */
        LocalDateTime startDate,
        /* @종료 날짜 */
        LocalDateTime endDate
) {
}

