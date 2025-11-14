package com.pos.commerce.application.shop;

import java.util.List;
import java.util.Optional;

import com.pos.commerce.application.shop.command.ApproveShopMemberCommand;
import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.command.RegisterShopMemberCommand;
import com.pos.commerce.application.shop.command.RejectShopMemberCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;
import com.pos.commerce.application.shop.query.GetShopMembershipsQuery;
import com.pos.commerce.application.shop.query.GetShopsByOwnerQuery;
import com.pos.commerce.domain.shop.Shop;
import com.pos.commerce.domain.shop.ShopMembership;

public interface ShopService {
    Shop createShop(CreateShopCommand command);
    Optional<Shop> getShopById(GetShopByIdQuery query);
    List<Shop> getShopsByOwner(GetShopsByOwnerQuery query);
    ShopMembership registerMember(RegisterShopMemberCommand command);
    ShopMembership approveMember(ApproveShopMemberCommand command);
    ShopMembership rejectMember(RejectShopMemberCommand command);
    List<ShopMembership> getMembers(GetShopMembershipsQuery query);
}




