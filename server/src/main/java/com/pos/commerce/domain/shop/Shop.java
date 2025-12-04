package com.pos.commerce.domain.shop;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shops")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 매장 코드 */
    @Column(nullable = false, unique = true)
    private String shopCode;

    /* 매장 비밀번호 */
    @Column(nullable = false)
    private String password;

    /* 관리자 비밀번호 */
    @Column(nullable = false)
    private String adminPassword;

    /* 생성일시 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* 수정일시 */
    private LocalDateTime updatedAt;


    /* 생성 */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /* 수정 */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}





