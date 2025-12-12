package com.pos.commerce.presentation.shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

/* @관리자 비밀번호 검증 요청 */
@Schema(description = "관리자 비밀번호 검증 요청")
public class VerifyAdminPasswordRequest {

    /* @관리자 비밀번호 */
    @NotBlank(message = "관리자 비밀번호는 필수 입력값입니다.")
    @Schema(description = "관리자 비밀번호" , example = "admin123")
    private String password;
}

