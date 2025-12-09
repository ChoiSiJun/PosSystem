package com.pos.commerce.presentation.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pos.commerce.presentation.common.dto.ApiResponse;

// 💡 개선 1: @RestControllerAdvice 추가 (전역 예외 처리를 위해 필수)
@RestControllerAdvice 
public class GlobalExceptionHandler {

    /* 로거 */
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /* 서버 내부 오류 처리 */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInternalServerError(RuntimeException e) {
        log.error("Internal Server Error: ", e); 
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body(ApiResponse.error(e.getMessage()));
    }
}