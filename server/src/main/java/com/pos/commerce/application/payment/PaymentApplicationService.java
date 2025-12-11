package com.pos.commerce.application.payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.commerce.application.payment.command.CancelPaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand.PaymentItemCommand;
import com.pos.commerce.application.payment.query.GetPaymentByIdQuery;
import com.pos.commerce.application.payment.query.GetPaymentByNumberQuery;
import com.pos.commerce.application.payment.query.GetPaymentsByDateRangeQuery;
import com.pos.commerce.domain.payment.Payment;
import com.pos.commerce.domain.payment.PaymentItem;
import com.pos.commerce.domain.payment.PaymentStatus;
import com.pos.commerce.domain.product.Product;
import com.pos.commerce.infrastructure.payment.repository.PaymentRepository;
import com.pos.commerce.infrastructure.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentApplicationService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;

    /* @결제 생성 */
    @Override
    public Payment createPayment(CreatePaymentCommand command) {
        Payment payment = Payment.builder()
                .shopCode(command.shopCode())
                .paymentNumber(generatePaymentNumber())
                .totalAmount(command.totalAmount())
                .status(PaymentStatus.PENDING)
                .method(command.method())
                .build();

        if (command.items() != null) {
            command.items().forEach(itemCommand -> payment.addItem(toPaymentItem(itemCommand)));
        }

        payment.complete();

        /* @재고 수량 차감 */
        command.items().forEach(itemCommand -> {
            Product product = productRepository.findById(itemCommand.productId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + itemCommand.productId()));
            product.decreaseStock(itemCommand.quantity());
            productRepository.save(product);
        });

        return paymentRepository.save(payment);
    }

    /* @결제 조회 */
    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentById(GetPaymentByIdQuery query) {
        return paymentRepository.findById(query.paymentId());
    }

    /* @결제 번호 조회 */
    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentByNumber(GetPaymentByNumberQuery query) {
        return paymentRepository.findByPaymentNumber(query.paymentNumber());
    }

    /* @매장 코드와 날짜 범위로  완료된 결제 내역 조회 */
    @Override
    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByDateRange(GetPaymentsByDateRangeQuery query) {
        return paymentRepository.findByShopCodeAndStatusAndCreatedAtBetween( query.shopCode(), PaymentStatus.COMPLETED, query.startDate(), query.endDate());
    }

    /* @결제 취소 */
    @Override
    public Payment cancelPayment(CancelPaymentCommand command) {
        Payment payment = findPayment(command.paymentId());
        payment.cancel();

        /* @재고 수량 증가 */
        payment.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + item.getProductId()));
            product.increaseStock(item.getQuantity());
            productRepository.save(product);
        });

        return paymentRepository.save(payment);
    }

    /* @결제 찾기 */
    private Payment findPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제를 찾을 수 없습니다: " + paymentId));
    }

    /* @결제 아이템 변환 */
    private PaymentItem toPaymentItem(PaymentItemCommand itemCommand) {
        return PaymentItem.builder()
                .productId(itemCommand.productId())
                .productName(itemCommand.productName())
                .quantity(itemCommand.quantity())
                .unitPrice(itemCommand.unitPrice())
                .totalPrice(itemCommand.unitPrice().multiply(java.math.BigDecimal.valueOf(itemCommand.quantity())))
                .build();
    }

    /* @결제 번호 생성 */
    private String generatePaymentNumber() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}




