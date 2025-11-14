package com.pos.commerce.application.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.commerce.application.shop.command.ApproveShopMemberCommand;
import com.pos.commerce.application.shop.command.CreateShopCommand;
import com.pos.commerce.application.shop.command.RegisterShopMemberCommand;
import com.pos.commerce.application.shop.command.RejectShopMemberCommand;
import com.pos.commerce.application.shop.query.GetShopByIdQuery;
import com.pos.commerce.application.shop.query.GetShopMembershipsQuery;
import com.pos.commerce.application.shop.query.GetShopsByOwnerQuery;
import com.pos.commerce.domain.shop.Shop;
import com.pos.commerce.domain.shop.ShopMemberRole;
import com.pos.commerce.domain.shop.ShopMembership;
import com.pos.commerce.domain.shop.ShopMembershipStatus;
import com.pos.commerce.domain.shop.ShopStatus;
import com.pos.commerce.domain.user.Role;
import com.pos.commerce.domain.user.User;
import com.pos.commerce.infrastructure.shop.repository.ShopMembershipRepository;
import com.pos.commerce.infrastructure.shop.repository.ShopRepository;
import com.pos.commerce.infrastructure.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopApplicationService implements ShopService {

    private final ShopRepository shopRepository;
    private final ShopMembershipRepository shopMembershipRepository;
    private final UserRepository userRepository;

    @Override
    public Shop createShop(CreateShopCommand command) {
        if (shopRepository.findByName(command.name()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 매장 이름입니다: " + command.name());
        }

        User owner = userRepository.findById(command.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + command.ownerId()));

        if (!canManageShop(owner)) {
            throw new IllegalStateException("매장을 생성할 권한이 없습니다.");
        }

        Shop shop = Shop.builder()
                .name(command.name())
                .description(command.description())
                .address(command.address())
                .status(ShopStatus.ACTIVE)
                .owner(owner)
                .build();

        Shop savedShop = shopRepository.save(shop);

        ShopMembership ownerMembership = ShopMembership.builder()
                .shop(savedShop)
                .user(owner)
                .role(ShopMemberRole.MANAGER)
                .status(ShopMembershipStatus.PENDING)
                .build();
        ownerMembership.approve();
        shopMembershipRepository.save(ownerMembership);

        return savedShop;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shop> getShopById(GetShopByIdQuery query) {
        return shopRepository.findById(query.shopId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shop> getShopsByOwner(GetShopsByOwnerQuery query) {
        return shopRepository.findByOwnerId(query.ownerId());
    }

    @Override
    public ShopMembership registerMember(RegisterShopMemberCommand command) {
        Shop shop = shopRepository.findById(command.shopId())
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다: " + command.shopId()));
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + command.userId()));

        if (shopMembershipRepository.existsByShopIdAndUserId(shop.getId(), user.getId())) {
            throw new IllegalStateException("이미 가입 요청이 존재합니다.");
        }

        ShopMembership membership = ShopMembership.builder()
                .shop(shop)
                .user(user)
                .role(ShopMemberRole.MEMBER)
                .status(ShopMembershipStatus.PENDING)
                .build();

        return shopMembershipRepository.save(membership);
    }

    @Override
    public ShopMembership approveMember(ApproveShopMemberCommand command) {
        ShopMembership membership = getMembershipForAction(command.shopId(), command.membershipId());
        validateManager(command.shopId(), command.approverId());

        if (membership.getStatus() != ShopMembershipStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 가입 요청입니다.");
        }

        membership.approve();
        return shopMembershipRepository.save(membership);
    }

    @Override
    public ShopMembership rejectMember(RejectShopMemberCommand command) {
        ShopMembership membership = getMembershipForAction(command.shopId(), command.membershipId());
        validateManager(command.shopId(), command.approverId());

        if (membership.getStatus() != ShopMembershipStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 가입 요청입니다.");
        }

        membership.reject();
        return shopMembershipRepository.save(membership);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopMembership> getMembers(GetShopMembershipsQuery query) {
        return shopMembershipRepository.findByShopId(query.shopId());
    }

    private boolean canManageShop(User owner) {
        return owner.getRoles().contains(Role.ADMIN) || owner.getRoles().contains(Role.MANAGER);
    }

    private void validateManager(Long shopId, Long approverId) {
        ShopMembership managerMembership = shopMembershipRepository.findByShopIdAndUserId(shopId, approverId)
                .orElseThrow(() -> new IllegalStateException("매장 관리자만 승인할 수 있습니다."));

        if (managerMembership.getRole() != ShopMemberRole.MANAGER
                || managerMembership.getStatus() != ShopMembershipStatus.APPROVED) {
            throw new IllegalStateException("승인 권한이 없습니다.");
        }
    }

    private ShopMembership getMembershipForAction(Long shopId, Long membershipId) {
        ShopMembership membership = shopMembershipRepository.findById(membershipId)
                .orElseThrow(() -> new IllegalArgumentException("가입 신청을 찾을 수 없습니다: " + membershipId));

        if (!membership.getShop().getId().equals(shopId)) {
            throw new IllegalArgumentException("매장 정보가 일치하지 않습니다.");
        }
        return membership;
    }
}




