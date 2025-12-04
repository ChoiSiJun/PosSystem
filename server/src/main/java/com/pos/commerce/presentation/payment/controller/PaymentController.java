package com.pos.commerce.presentation.payment.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.commerce.application.payment.PaymentService;
import com.pos.commerce.application.payment.command.CancelPaymentCommand;
import com.pos.commerce.application.payment.command.CompletePaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand;
import com.pos.commerce.application.payment.command.ProcessPaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand.PaymentItemCommand;
import com.pos.commerce.application.payment.query.GetPaymentByIdQuery;
import com.pos.commerce.application.payment.query.GetPaymentsByUserIdQuery;
import com.pos.commerce.domain.payment.Payment;
import com.pos.commerce.presentation.common.dto.ApiResponse;
import com.pos.commerce.presentation.payment.dto.PaymentItemRequest;
import com.pos.commerce.presentation.payment.dto.PaymentRequest;
import com.pos.commerce.presentation.payment.dto.PaymentResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /* @결제 생성 */
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@RequestBody PaymentRequest request) {
        List<PaymentItemCommand> items = request.getItems() != null
                ? request.getItems().stream()
                .map(this::toItemCommand)
                .collect(Collectors.toList())
                : List.of();

        CreatePaymentCommand command = new CreatePaymentCommand(
                request.getTotalAmount(),
                request.getMethod(),
                request.getUserId(),
                items
        );

        Payment created = paymentService.createPayment(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("결제가 생성되었습니다.", PaymentResponse.from(created)));
    }

    /* @결제 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(@PathVariable Long id) {
        return paymentService.getPaymentById(new GetPaymentByIdQuery(id))
                .map(payment -> ResponseEntity.ok(ApiResponse.success(PaymentResponse.from(payment))))
                .orElse(ResponseEntity.notFound().build());
    }

    /* @사용자별 결제 조회 */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByUser(@PathVariable String userId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByUserId(new GetPaymentsByUserIdQuery(userId)).stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(payments));
    }

    /* @결제 처리 */
    @PostMapping("/{id}/process")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@PathVariable Long id) {
        Payment processed = paymentService.processPayment(new ProcessPaymentCommand(id));
        return ResponseEntity.ok(ApiResponse.success("결제가 처리되었습니다.", PaymentResponse.from(processed)));
    }

    /* @결제 취소 */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(@PathVariable Long id) {
        Payment cancelled = paymentService.cancelPayment(new CancelPaymentCommand(id));
        return ResponseEntity.ok(ApiResponse.success("결제가 취소되었습니다.", PaymentResponse.from(cancelled)));
    }

    /* @결제 완료 */
    @PostMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<PaymentResponse>> completePayment(@PathVariable Long id) {
        Payment completed = paymentService.completePayment(new CompletePaymentCommand(id));
        return ResponseEntity.ok(ApiResponse.success("결제가 완료되었습니다.", PaymentResponse.from(completed)));
    }

    /* @결제 아이템 변환 */
    private PaymentItemCommand toItemCommand(PaymentItemRequest itemRequest) {
        return new PaymentItemCommand(
                itemRequest.getProductId(),
                itemRequest.getProductName(),
                itemRequest.getQuantity(),
                itemRequest.getUnitPrice()
        );
    }
}

