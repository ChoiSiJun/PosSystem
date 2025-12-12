package com.pos.commerce.config.handler;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pos.commerce.presentation.common.dto.ApiResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    /* 로거 */
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /* 유효성 검증 예외 처리 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        
    // 💡 1. BindingResult에서 첫 번째 필드 에러를 가져옴
    FieldError firstError = e.getBindingResult().getFieldError();
    String errorMessage = firstError != null ? firstError.getDefaultMessage() : "입력값 검증에 실패했습니다."; 
 
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) 
                .body(ApiResponse.error(errorMessage));
    }


    /* 서버 내부 오류 처리 */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInternalServerError(RuntimeException e) {
        log.error("Internal Server Error: ", e); 
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) 
                .body(ApiResponse.error(e.getMessage()));
    }
}