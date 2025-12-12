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

/* @매장 생성 요청 */
@Schema(description = "매장 생성 요청")
public class CreateShopRequest {

    /* @매장 코드 */
    @NotBlank(message = "매장 코드는 필수 입력값입니다.")
    @Schema(description = "매장 코드" , example = "shop123")
    private String shopCode;

    /* @매장 이름 */
    @NotBlank(message = "매장 이름은 필수 입력값입니다.")
    @Schema(description = "매장 이름" , example = "매장1")
    private String shopName;

    /* @매장 비밀번호 */
    @NotBlank(message = "매장 비밀번호는 필수 입력값입니다.")
    @Schema(description = "매장 비밀번호" , example = "password123")
    private String password;

    /* @관리자 비밀번호 */
    @NotBlank(message = "관리자 비밀번호는 필수 입력값입니다.")
    @Schema(description = "관리자 비밀번호" , example = "admin123")
    private String adminPassword;
}





