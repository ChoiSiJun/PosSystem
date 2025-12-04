package com.pos.commerce.presentation.inventory.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.commerce.application.inventory.InventoryService;
import com.pos.commerce.application.inventory.command.ConfirmInventoryReservationCommand;
import com.pos.commerce.application.inventory.command.CreateInventoryCommand;
import com.pos.commerce.application.inventory.command.DecreaseInventoryCommand;
import com.pos.commerce.application.inventory.command.IncreaseInventoryCommand;
import com.pos.commerce.application.inventory.command.ReleaseInventoryReservationCommand;
import com.pos.commerce.application.inventory.command.ReserveInventoryCommand;
import com.pos.commerce.application.inventory.query.GetAllInventoriesQuery;
import com.pos.commerce.application.inventory.query.GetInventoryByProductIdQuery;
import com.pos.commerce.application.inventory.query.GetInventoryTransactionHistoryQuery;
import com.pos.commerce.domain.inventory.Inventory;
import com.pos.commerce.domain.inventory.InventoryTransaction;
import com.pos.commerce.presentation.common.dto.ApiResponse;
import com.pos.commerce.presentation.inventory.dto.InventoryResponse;
import com.pos.commerce.presentation.inventory.dto.StockRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /* @재고 생성 */
    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> createInventory(
            @PathVariable Long productId,
            @RequestBody Integer initialQuantity) {
        Inventory inventory = inventoryService.createInventory(new CreateInventoryCommand(productId, initialQuantity));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("재고가 생성되었습니다.", InventoryResponse.from(inventory)));
    }

    /* @재고 조회 */
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventoryByProduct(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(new GetInventoryByProductIdQuery(productId))
                .map(inventory -> ResponseEntity.ok(ApiResponse.success(InventoryResponse.from(inventory))))
                .orElse(ResponseEntity.notFound().build());
    }

    /* @모든 재고 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAllInventories() {
        List<InventoryResponse> inventories = inventoryService.getAllInventories(new GetAllInventoriesQuery()).stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(inventories));
    }

    /* @재고 증가 */
    @PostMapping("/{productId}/increase")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> increaseStock(
            @PathVariable Long productId,
            @RequestBody StockRequest request,
            Authentication authentication) {
        String createdBy = authentication != null ? authentication.getName() : "SYSTEM";
        Inventory inventory = inventoryService.increaseStock(
                new IncreaseInventoryCommand(
                        productId,
                        request.getQuantity(),
                        request.getDescription() != null ? request.getDescription() : "재고 증가",
                        createdBy
                ));
        return ResponseEntity.ok(ApiResponse.success("재고가 증가되었습니다.", InventoryResponse.from(inventory)));
    }

    /* @재고 감소 */
    @PostMapping("/{productId}/decrease")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> decreaseStock(
            @PathVariable Long productId,
            @RequestBody StockRequest request,
            Authentication authentication) {
        String createdBy = authentication != null ? authentication.getName() : "SYSTEM";
        Inventory inventory = inventoryService.decreaseStock(
                new DecreaseInventoryCommand(
                        productId,
                        request.getQuantity(),
                        request.getDescription() != null ? request.getDescription() : "재고 감소",
                        createdBy
                ));
        return ResponseEntity.ok(ApiResponse.success("재고가 감소되었습니다.", InventoryResponse.from(inventory)));
    }

    /* @재고 예약 */
    @PostMapping("/{productId}/reserve")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> reserveStock(
            @PathVariable Long productId,
            @RequestBody StockRequest request,
            Authentication authentication) {
        String createdBy = authentication != null ? authentication.getName() : "SYSTEM";
        Inventory inventory = inventoryService.reserveStock(
                new ReserveInventoryCommand(
                        productId,
                        request.getQuantity(),
                        request.getDescription() != null ? request.getDescription() : "재고 예약",
                        createdBy
                ));
        return ResponseEntity.ok(ApiResponse.success("재고가 예약되었습니다.", InventoryResponse.from(inventory)));
    }

    /* @재고 예약 해제 */
    @PostMapping("/{productId}/release")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> releaseReservation(
            @PathVariable Long productId,
            @RequestBody StockRequest request,
            Authentication authentication) {
        String createdBy = authentication != null ? authentication.getName() : "SYSTEM";
        Inventory inventory = inventoryService.releaseReservation(
                new ReleaseInventoryReservationCommand(
                        productId,
                        request.getQuantity(),
                        request.getDescription() != null ? request.getDescription() : "예약 해제",
                        createdBy
                ));
        return ResponseEntity.ok(ApiResponse.success("예약이 해제되었습니다.", InventoryResponse.from(inventory)));
    }

    /* @재고 예약 확정 */
    @PostMapping("/{productId}/confirm")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<InventoryResponse>> confirmReservation(
            @PathVariable Long productId,
            @RequestBody StockRequest request,
            Authentication authentication) {
        String createdBy = authentication != null ? authentication.getName() : "SYSTEM";
        Inventory inventory = inventoryService.confirmReservation(
                new ConfirmInventoryReservationCommand(
                        productId,
                        request.getQuantity(),
                        request.getDescription() != null ? request.getDescription() : "예약 확정",
                        createdBy
                ));
        return ResponseEntity.ok(ApiResponse.success("예약이 확정되었습니다.", InventoryResponse.from(inventory)));
    }

    /* @재고 거래 내역 조회 */
    @GetMapping("/{productId}/transactions")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<InventoryTransaction>>> getTransactionHistory(@PathVariable Long productId) {
        List<InventoryTransaction> transactions = inventoryService.getTransactionHistory(
                new GetInventoryTransactionHistoryQuery(productId));
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
}

