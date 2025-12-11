package com.pos.commerce.application.payment;

import java.util.List;
import java.util.Optional;

import com.pos.commerce.application.payment.command.CancelPaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand;
import com.pos.commerce.application.payment.command.DeletePaymentCommand;
import com.pos.commerce.application.payment.command.PreparePaymentCommand;
import com.pos.commerce.application.payment.query.GetPaymentByIdQuery;
import com.pos.commerce.application.payment.query.GetPaymentByNumberQuery;
import com.pos.commerce.application.payment.query.GetPaymentsByDateRangeQuery;
import com.pos.commerce.application.payment.query.GetPreparedPaymentsQuery;
import com.pos.commerce.domain.payment.Payment;

public interface PaymentService {
    Payment createPayment(CreatePaymentCommand command);
    Payment preparePayment(PreparePaymentCommand command);
    Optional<Payment> getPaymentById(GetPaymentByIdQuery query);
    Optional<Payment> getPaymentByNumber(GetPaymentByNumberQuery query);
    List<Payment> getPaymentsByDateRange(GetPaymentsByDateRangeQuery query);
    List<Payment> getPreparedPayments(GetPreparedPaymentsQuery query);
    Payment cancelPayment(CancelPaymentCommand command);
    void deletePayment(DeletePaymentCommand command);
}

