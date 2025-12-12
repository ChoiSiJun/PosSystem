package com.pos.commerce.presentation.shop.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pos.commerce.application.shop.ShopService;
import com.pos.commerce.application.shop.command.AuthenticationShopCommand;
import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.command.VerifyAdminPasswordCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;
import com.pos.commerce.presentation.common.dto.ApiResponse;

import com.pos.commerce.presentation.shop.dto.CreateShopRequest;
import com.pos.commerce.presentation.shop.dto.DetailShopResponse;
import com.pos.commerce.presentation.shop.dto.LoginShopRequest;
import com.pos.commerce.presentation.shop.dto.VerifyAdminPasswordRequest;
import com.pos.commerce.presentation.shop.dto.VerifyAdminPasswordResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@Tag(name = "매장관리", description = "매장 관리 API")
@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor

/* @매장 컨트롤러 */
public class ShopController {

    /* @매장 서비스 */
    private final ShopService shopService;

    /* @매장 생성 API */
    @Operation(summary = "매장 생성", description = "매장을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createShop(@Valid @RequestBody CreateShopRequest request) {

        /* @매장 생성 명령 */
        shopService.createShop(
                new CreateShopCommand(
                        request.getShopCode(),
                        request.getShopName(),
                        request.getPassword(),
                        request.getAdminPassword()
                )
        );

        /* @매장 생성 응답 */
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("매장이 생성되었습니다."));
    }

    /* @매장 조회 */
    @Operation(summary = "매장 조회", description = "매장을 조회합니다.")
    @GetMapping("/{shopId}")
    public ResponseEntity<ApiResponse<DetailShopResponse>> getShop(@PathVariable Long shopId) {
        return ResponseEntity.ok(ApiResponse.success(DetailShopResponse.from(shopService.getShopById(new GetShopByIdQuery(shopId)))));
    }

    /* @매장 로그인 */
    @Operation(summary = "매장 로그인", description = "매장을 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginShop(@Valid @RequestBody LoginShopRequest request) {
       
        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", 
                shopService.loginShop(new AuthenticationShopCommand(request.getShopCode(), request.getPassword())))
        );
    }

    /* @관리자 비밀번호 검증 */
    @Operation(summary = "관리자 비밀번호 검증", description = "관리자 비밀번호를 검증합니다.")
    @PostMapping("/{shopCode}/admin/verify")
    public ResponseEntity<ApiResponse<VerifyAdminPasswordResponse>> verifyAdminPassword(
            @PathVariable String shopCode,
            @Valid @RequestBody VerifyAdminPasswordRequest request) {
        
        boolean verified = shopService.verifyAdminPassword(
                new VerifyAdminPasswordCommand(shopCode, request.getPassword()));
        
        VerifyAdminPasswordResponse response = VerifyAdminPasswordResponse.builder()
                .verified(verified)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}




