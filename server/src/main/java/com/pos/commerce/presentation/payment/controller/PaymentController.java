package com.pos.commerce.presentation.payment.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import com.pos.commerce.application.payment.PaymentService;
import com.pos.commerce.application.payment.command.CancelPaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand;
import com.pos.commerce.application.payment.command.CreatePaymentCommand.PaymentItemCommand;
import com.pos.commerce.application.payment.query.GetPaymentByIdQuery;
import com.pos.commerce.application.payment.query.GetPaymentsByDateRangeQuery;
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
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @RequestBody PaymentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        /* @결제 아이템 */
        List<PaymentItemCommand> items = request.getItems() != null
                ? request.getItems().stream()
                .map(this::toItemCommand)
                .collect(Collectors.toList())
                : List.of();

        CreatePaymentCommand command = new CreatePaymentCommand(
                /* @매장 코드 */
                userDetails.getUsername(),
                /* @결제 총 금액 */
                request.getTotalAmount(),
                /* @결제 방법 */
                request.getMethod(),
                /* @결제 아이템 */
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

    /* @날짜 범위로 결제 내역 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByDateRange(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate,
        @AuthenticationPrincipal UserDetails userDetails) {
             
        List<Payment> payments = paymentService.getPaymentsByDateRange(
                new GetPaymentsByDateRangeQuery(
                userDetails.getUsername(),      
                startDate, endDate.plusDays(1).withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)));
        List<PaymentResponse> responses = payments.stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /* @결제 취소 */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(@PathVariable Long id) {
        Payment cancelled = paymentService.cancelPayment(new CancelPaymentCommand(id));
        return ResponseEntity.ok(ApiResponse.success("결제가 취소되었습니다.", PaymentResponse.from(cancelled)));
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

