package com.pos.commerce.application.shop.command;

public record ApproveShopMemberCommand(
        Long shopId,
        Long membershipId,
        Long approverId
) {
}





