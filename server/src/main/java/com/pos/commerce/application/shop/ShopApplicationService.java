package com.pos.commerce.application.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.commerce.application.shop.command.AuthenticationShopCommand;
import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.command.VerifyAdminPasswordCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;
import com.pos.commerce.domain.shop.Shop;
import com.pos.commerce.infrastructure.shop.repository.ShopRepository;
import com.pos.commerce.util.JwtProvider;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class ShopApplicationService implements ShopService {

    private final ShopRepository shopRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    @Override
    public Shop createShop(CreateShopCommand command) {

        /* 매장 코드 중복 체크 */
        if (shopRepository.findByShopCode(command.shopCode()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 매장 이름입니다: " + command.shopCode());
        }

        /* 매장 생성 */
        Shop shop = Shop.builder()
                .shopCode(command.shopCode())
                .shopName(command.shopName())
                .password(passwordEncoder.encode(command.password()))
                .adminPassword(passwordEncoder.encode(command.adminPassword()))
                .build();

        /* 매장 저장 */
        Shop savedShop = shopRepository.save(shop);

        /* 매장 반환 */
        return savedShop;
    }

    @Override
    @Transactional(readOnly = true)
    public Shop getShopById(GetShopByIdQuery query) {
        /* 매장 조회 */
        return shopRepository.findById(query.shopId()).orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다: " + query.shopId()));
    }

    @Override
    public String loginShop(AuthenticationShopCommand command) {

        /* 매장 코드 조회 */
        Shop shop = shopRepository.findByShopCode(command.shopCode())
        .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다: " + command.shopCode()));

        /* 매장 비밀번호 체크 */
        if (!passwordEncoder.matches(command.password(), shop.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        /* 매장 반환 */
        Map<String,String> claim = new HashMap<>();
        claim.put( "shopCode", shop.getShopCode());
        claim.put("shopName", shop.getShopName());
        return jwtProvider.generateToken(shop.getShopCode(), claim);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyAdminPassword(VerifyAdminPasswordCommand command) {
        /* 매장 코드 조회 */
        Shop shop = shopRepository.findByShopCode(command.shopCode())
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다: " + command.shopCode()));

        /* 관리자 비밀번호 검증 */
        return passwordEncoder.matches(command.password(), shop.getAdminPassword());
    }
}




