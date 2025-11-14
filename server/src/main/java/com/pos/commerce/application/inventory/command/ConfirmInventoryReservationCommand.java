package com.pos.commerce.application.inventory.command;

public record ConfirmInventoryReservationCommand(
        Long productId,
        Integer quantity,
        String description,
        String createdBy
) {
}





