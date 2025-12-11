package com.pos.commerce.application.payment.query;

import java.time.LocalDateTime;
public record GetPaymentsByDateRangeQuery(

        /* @매장 코드 */
        String shopCode,

        /* @시작 날짜 */
        LocalDateTime startDate,
        /* @종료 날짜 */
        LocalDateTime endDate
) {
}

