package com.pos.commerce.infrastructure.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.commerce.domain.shop.ShopMembership;

@Repository
public interface ShopMembershipRepository extends JpaRepository<ShopMembership, Long> {
    boolean existsByShopIdAndUserId(Long shopId, Long userId);
    Optional<ShopMembership> findByShopIdAndUserId(Long shopId, Long userId);
    List<ShopMembership> findByShopId(Long shopId);
}




