package com.pos.commerce.application.shop;
 
import java.util.Optional;

import com.pos.commerce.application.shop.command.AuthenticationShopCommand;
import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;
import com.pos.commerce.domain.shop.Shop;


public interface ShopService {

    /* @매장 생성 */
    Shop createShop(CreateShopCommand command);

    /* @매장 조회 */
    Optional<Shop> getShopById(GetShopByIdQuery query);

    /* @매장 로그인 */
    String loginShop(AuthenticationShopCommand command);

    
}




