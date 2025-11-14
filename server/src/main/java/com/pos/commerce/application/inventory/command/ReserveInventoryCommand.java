package com.pos.commerce.application.inventory.command;

public record ReserveInventoryCommand(
        Long productId,
        Integer quantity,
        String description,
        String createdBy
) {
}





