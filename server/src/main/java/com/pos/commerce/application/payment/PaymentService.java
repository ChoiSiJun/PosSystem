package com.pos.commerce.application.payment;

import java.util.List;
import java.util.Optional;

import com.pos.commerce.application.payment.command.CancelPaymentCommand;
import com.pos.commerce.application.payment.command.CompletePaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand;
import com.pos.commerce.application.payment.command.ProcessPaymentCommand;
import com.pos.commerce.application.payment.query.GetPaymentByIdQuery;
import com.pos.commerce.application.payment.query.GetPaymentByNumberQuery;
import com.pos.commerce.application.payment.query.GetPaymentsByUserIdQuery;
import com.pos.commerce.domain.payment.Payment;

public interface PaymentService {
    Payment createPayment(CreatePaymentCommand command);
    Optional<Payment> getPaymentById(GetPaymentByIdQuery query);
    Optional<Payment> getPaymentByNumber(GetPaymentByNumberQuery query);
    List<Payment> getPaymentsByUserId(GetPaymentsByUserIdQuery query);
    Payment processPayment(ProcessPaymentCommand command);
    Payment cancelPayment(CancelPaymentCommand command);
    Payment completePayment(CompletePaymentCommand command);
}

