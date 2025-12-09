package com.pos.commerce.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    /**
     * 파일을 저장하고 저장된 파일의 URL을 반환합니다.
     * 
     * @param file 저장할 파일
     * @return 저장된 파일의 URL 또는 경로
     * @throws IllegalArgumentException 파일이 비어있거나 저장에 실패한 경우
     */
    String saveFile(MultipartFile file);
}

