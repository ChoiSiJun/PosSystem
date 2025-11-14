package com.pos.commerce.application.inventory.command;

public record ReleaseInventoryReservationCommand(
        Long productId,
        Integer quantity,
        String description,
        String createdBy
) {
}





