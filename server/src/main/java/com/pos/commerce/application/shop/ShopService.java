package com.pos.commerce.application.shop;
 
import java.util.Optional;
import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;
import com.pos.commerce.domain.shop.Shop;

public interface ShopService {
    Shop createShop(CreateShopCommand command);
    Optional<Shop> getShopById(GetShopByIdQuery query);
}




