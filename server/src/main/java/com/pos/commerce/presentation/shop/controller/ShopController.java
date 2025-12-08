package com.pos.commerce.presentation.shop.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.commerce.application.shop.ShopService;
import com.pos.commerce.application.shop.command.AuthenticationShopCommand;
import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;

import com.pos.commerce.domain.shop.Shop;
import com.pos.commerce.presentation.common.dto.ApiResponse;

import com.pos.commerce.presentation.shop.dto.ShopRequest;
import com.pos.commerce.presentation.shop.dto.ShopResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    /* @매장 생성 */
    @PostMapping
    public ResponseEntity<ApiResponse<ShopResponse>> createShop(@RequestBody ShopRequest request) {
        Shop shop = shopService.createShop(new CreateShopCommand(
                request.getShopCode(),
                request.getShopName(),
                request.getPassword(),
                request.getAdminPassword()
        ));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("매장이 생성되었습니다.", ShopResponse.from(shop)));
    }

    /* @매장 조회 */
    @GetMapping("/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> getShop(@PathVariable Long shopId) {
        return shopService.getShopById(new GetShopByIdQuery(shopId))
                .map(shop -> ResponseEntity.ok(ApiResponse.success(ShopResponse.from(shop))))
                .orElse(ResponseEntity.notFound().build());
    }

    /* @매장 로그인 */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginShop(@RequestBody ShopRequest request) {
       
        String token = shopService.loginShop(new AuthenticationShopCommand(request.getShopCode(), request.getPassword()));
        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", token));
    }
}




