package com.pos.commerce.application.payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.commerce.application.payment.command.CancelPaymentCommand;
import com.pos.commerce.application.payment.command.CompletePaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand;
import com.pos.commerce.application.payment.command.ProcessPaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand.PaymentItemCommand;
import com.pos.commerce.application.payment.query.GetPaymentByIdQuery;
import com.pos.commerce.application.payment.query.GetPaymentByNumberQuery;
import com.pos.commerce.application.payment.query.GetPaymentsByUserIdQuery;
import com.pos.commerce.domain.payment.Payment;
import com.pos.commerce.domain.payment.PaymentItem;
import com.pos.commerce.domain.payment.PaymentStatus;
import com.pos.commerce.infrastructure.payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentApplicationService implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(CreatePaymentCommand command) {
        Payment payment = Payment.builder()
                .paymentNumber(generatePaymentNumber())
                .totalAmount(command.totalAmount())
                .status(PaymentStatus.PENDING)
                .method(command.method())
                .userId(command.userId())
                .build();

        if (command.items() != null) {
            command.items().forEach(itemCommand -> payment.addItem(toPaymentItem(itemCommand)));
        }

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentById(GetPaymentByIdQuery query) {
        return paymentRepository.findById(query.paymentId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentByNumber(GetPaymentByNumberQuery query) {
        return paymentRepository.findByPaymentNumber(query.paymentNumber());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByUserId(GetPaymentsByUserIdQuery query) {
        return paymentRepository.findByUserId(query.userId());
    }

    @Override
    public Payment processPayment(ProcessPaymentCommand command) {
        Payment payment = findPayment(command.paymentId());
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException("처리할 수 없는 결제 상태입니다: " + payment.getStatus());
        }
        payment.complete();
        return paymentRepository.save(payment);
    }

    @Override
    public Payment cancelPayment(CancelPaymentCommand command) {
        Payment payment = findPayment(command.paymentId());
        payment.cancel();
        return paymentRepository.save(payment);
    }

    @Override
    public Payment completePayment(CompletePaymentCommand command) {
        Payment payment = findPayment(command.paymentId());
        payment.complete();
        return paymentRepository.save(payment);
    }

    private Payment findPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다: " + paymentId));
    }

    private PaymentItem toPaymentItem(PaymentItemCommand itemCommand) {
        return PaymentItem.builder()
                .productId(itemCommand.productId())
                .productName(itemCommand.productName())
                .quantity(itemCommand.quantity())
                .unitPrice(itemCommand.unitPrice())
                .totalPrice(itemCommand.unitPrice().multiply(java.math.BigDecimal.valueOf(itemCommand.quantity())))
                .build();
    }

    private String generatePaymentNumber() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}




