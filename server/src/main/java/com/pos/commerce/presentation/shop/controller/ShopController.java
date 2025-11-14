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
import com.pos.commerce.application.shop.command.ApproveShopMemberCommand;
import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.command.RegisterShopMemberCommand;
import com.pos.commerce.application.shop.command.RejectShopMemberCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;
import com.pos.commerce.application.shop.query.GetShopMembershipsQuery;
import com.pos.commerce.application.shop.query.GetShopsByOwnerQuery;
import com.pos.commerce.domain.shop.Shop;
import com.pos.commerce.domain.shop.ShopMembership;
import com.pos.commerce.presentation.common.dto.ApiResponse;
import com.pos.commerce.presentation.shop.dto.ShopMemberRequest;
import com.pos.commerce.presentation.shop.dto.ShopMembershipDecisionRequest;
import com.pos.commerce.presentation.shop.dto.ShopMembershipResponse;
import com.pos.commerce.presentation.shop.dto.ShopRequest;
import com.pos.commerce.presentation.shop.dto.ShopResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ShopResponse>> createShop(@RequestBody ShopRequest request) {
        Shop shop = shopService.createShop(new CreateShopCommand(
                request.getName(),
                request.getDescription(),
                request.getAddress(),
                request.getOwnerId()
        ));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("매장이 생성되었습니다.", ShopResponse.from(shop)));
    }

    @GetMapping("/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> getShop(@PathVariable Long shopId) {
        return shopService.getShopById(new GetShopByIdQuery(shopId))
                .map(shop -> ResponseEntity.ok(ApiResponse.success(ShopResponse.from(shop))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<ShopResponse>>> getShopsByOwner(@PathVariable Long ownerId) {
        List<ShopResponse> shops = shopService.getShopsByOwner(new GetShopsByOwnerQuery(ownerId)).stream()
                .map(ShopResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(shops));
    }

    @PostMapping("/{shopId}/members")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ShopMembershipResponse>> requestMembership(
            @PathVariable Long shopId,
            @RequestBody ShopMemberRequest request) {
        ShopMembership membership = shopService.registerMember(new RegisterShopMemberCommand(
                shopId,
                request.getUserId()
        ));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("가입 요청이 접수되었습니다.", ShopMembershipResponse.from(membership)));
    }

    @PatchMapping("/{shopId}/members/{membershipId}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ShopMembershipResponse>> approveMembership(
            @PathVariable Long shopId,
            @PathVariable Long membershipId,
            @RequestBody ShopMembershipDecisionRequest request) {
        ShopMembership membership = shopService.approveMember(new ApproveShopMemberCommand(
                shopId,
                membershipId,
                request.getApproverId()
        ));
        return ResponseEntity.ok(ApiResponse.success("가입 요청이 승인되었습니다.", ShopMembershipResponse.from(membership)));
    }

    @PatchMapping("/{shopId}/members/{membershipId}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<ShopMembershipResponse>> rejectMembership(
            @PathVariable Long shopId,
            @PathVariable Long membershipId,
            @RequestBody ShopMembershipDecisionRequest request) {
        ShopMembership membership = shopService.rejectMember(new RejectShopMemberCommand(
                shopId,
                membershipId,
                request.getApproverId()
        ));
        return ResponseEntity.ok(ApiResponse.success("가입 요청이 거절되었습니다.", ShopMembershipResponse.from(membership)));
    }

    @GetMapping("/{shopId}/members")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<ShopMembershipResponse>>> getMemberships(@PathVariable Long shopId) {
        List<ShopMembershipResponse> memberships = shopService.getMembers(new GetShopMembershipsQuery(shopId)).stream()
                .map(ShopMembershipResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(memberships));
    }
}




