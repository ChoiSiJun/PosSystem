package com.pos.commerce.application.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;
import com.pos.commerce.domain.shop.Shop;
import com.pos.commerce.infrastructure.shop.repository.ShopRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class ShopApplicationService implements ShopService {

    private final ShopRepository shopRepository;

    @Override
    public Shop createShop(CreateShopCommand command) {

        /* 매장 코드 중복 체크 */
        if (shopRepository.findByShopCode(command.shopCode()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 매장 이름입니다: " + command.shopCode());
        }

        /* 매장 생성 */
        Shop shop = Shop.builder()
                .shopCode(command.shopCode())
                .password(command.password())
                .adminPassword(command.adminPassword())
                .build();

        /* 매장 저장 */
        Shop savedShop = shopRepository.save(shop);

        return savedShop;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shop> getShopById(GetShopByIdQuery query) {
        return shopRepository.findById(query.shopId());
    }
}




