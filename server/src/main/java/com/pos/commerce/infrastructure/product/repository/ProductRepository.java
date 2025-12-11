package com.pos.commerce.infrastructure.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.commerce.domain.product.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /* @상품 코드 조회 */
    Optional<Product> findByCode(String code);

    /* @매장 코드로 상품 조회 */
    List<Product> findByShopCode(String shopCode);

    /* @상품 코드 조회 */
    boolean existsByCode(String code);
}




