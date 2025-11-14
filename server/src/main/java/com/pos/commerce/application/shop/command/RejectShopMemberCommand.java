package com.pos.commerce.application.shop.command;

public record RejectShopMemberCommand(
        Long shopId,
        Long membershipId,
        Long approverId
) {
}





