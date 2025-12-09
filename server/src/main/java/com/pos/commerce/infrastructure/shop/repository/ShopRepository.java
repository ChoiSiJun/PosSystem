package com.pos.commerce.infrastructure.shop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.commerce.domain.shop.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    /* @매장 코드 조회 */
    Optional<Shop> findByShopCode(String shopCode);
    
    /* @매장 코드 중복 체크 */
    boolean existsByShopCode(String shopCode);
}





