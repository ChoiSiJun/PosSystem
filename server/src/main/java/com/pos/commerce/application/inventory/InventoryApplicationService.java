package com.pos.commerce.application.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.pos.commerce.domain.inventory.TransactionType;
import com.pos.commerce.infrastructure.inventory.repository.InventoryRepository;
import com.pos.commerce.infrastructure.inventory.repository.InventoryTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryApplicationService implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository transactionRepository;

    @Override
    public Inventory createInventory(CreateInventoryCommand command) {
        Long productId = command.productId();
        if (inventoryRepository.existsByProductId(productId)) {
            throw new IllegalArgumentException("이미 재고 정보가 존재합니다: " + productId);
        }

        Inventory inventory = Inventory.builder()
                .productId(productId)
                .quantity(command.initialQuantity())
                .reservedQuantity(0)
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);
        recordTransaction(savedInventory, TransactionType.IN, command.initialQuantity(), 0,
                "초기 재고 등록", "SYSTEM");
        return savedInventory;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inventory> getInventoryByProductId(GetInventoryByProductIdQuery query) {
        return inventoryRepository.findByProductId(query.productId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventories(GetAllInventoriesQuery query) {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory increaseStock(IncreaseInventoryCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        Integer before = inventory.getQuantity();
        inventory.increaseQuantity(command.quantity());
        Inventory saved = inventoryRepository.save(inventory);
        recordTransaction(saved, TransactionType.IN, command.quantity(), before,
                command.description(), command.createdBy());
        return saved;
    }

    @Override
    public Inventory decreaseStock(DecreaseInventoryCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        Integer before = inventory.getQuantity();
        inventory.decreaseQuantity(command.quantity());
        Inventory saved = inventoryRepository.save(inventory);
        recordTransaction(saved, TransactionType.OUT, command.quantity(), before,
                command.description(), command.createdBy());
        return saved;
    }

    @Override
    public Inventory reserveStock(ReserveInventoryCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        Integer before = inventory.getQuantity();
        inventory.reserveQuantity(command.quantity());
        Inventory saved = inventoryRepository.save(inventory);
        recordTransaction(saved, TransactionType.RESERVE, command.quantity(), before,
                command.description(), command.createdBy());
        return saved;
    }

    @Override
    public Inventory releaseReservation(ReleaseInventoryReservationCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        Integer before = inventory.getQuantity();
        inventory.releaseReservation(command.quantity());
        Inventory saved = inventoryRepository.save(inventory);
        recordTransaction(saved, TransactionType.RELEASE, command.quantity(), before,
                command.description(), command.createdBy());
        return saved;
    }

    @Override
    public Inventory confirmReservation(ConfirmInventoryReservationCommand command) {
        Inventory inventory = getInventoryOrThrow(command.productId());
        Integer before = inventory.getQuantity();
        inventory.confirmReservation(command.quantity());
        Inventory saved = inventoryRepository.save(inventory);
        recordTransaction(saved, TransactionType.OUT, command.quantity(), before,
                command.description(), command.createdBy());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTransaction> getTransactionHistory(GetInventoryTransactionHistoryQuery query) {
        return transactionRepository.findByProductIdOrderByCreatedAtDesc(query.productId());
    }

    private Inventory getInventoryOrThrow(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다: " + productId));
    }

    private void recordTransaction(
            Inventory inventory,
            TransactionType type,
            Integer quantity,
            Integer quantityBefore,
            String description,
            String createdBy
    ) {
        InventoryTransaction transaction = InventoryTransaction.builder()
                .productId(inventory.getProductId())
                .type(type)
                .quantity(quantity)
                .quantityBefore(quantityBefore)
                .quantityAfter(inventory.getQuantity())
                .description(description)
                .createdBy(createdBy)
                .build();
        transactionRepository.save(transaction);
    }
}
