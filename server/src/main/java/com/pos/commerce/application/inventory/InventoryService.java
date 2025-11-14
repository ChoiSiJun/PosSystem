package com.pos.commerce.application.inventory;

import java.util.List;
import java.util.Optional;

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

public interface InventoryService {
    Inventory createInventory(CreateInventoryCommand command);
    Optional<Inventory> getInventoryByProductId(GetInventoryByProductIdQuery query);
    List<Inventory> getAllInventories(GetAllInventoriesQuery query);
    Inventory increaseStock(IncreaseInventoryCommand command);
    Inventory decreaseStock(DecreaseInventoryCommand command);
    Inventory reserveStock(ReserveInventoryCommand command);
    Inventory releaseReservation(ReleaseInventoryReservationCommand command);
    Inventory confirmReservation(ConfirmInventoryReservationCommand command);
    List<InventoryTransaction> getTransactionHistory(GetInventoryTransactionHistoryQuery query);
}

