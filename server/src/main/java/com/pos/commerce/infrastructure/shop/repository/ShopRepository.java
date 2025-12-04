package com.pos.commerce.infrastructure.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.commerce.domain.shop.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findByShopCode(String shopCode);
    List<Shop> findByOwnerId(Long ownerId);
}





