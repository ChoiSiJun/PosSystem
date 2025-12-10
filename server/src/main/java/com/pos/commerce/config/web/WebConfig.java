package com.pos.commerce.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads/images}")
    private String uploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로 (/**)에 대해 CORS를 적용합니다.
        registry.addMapping("/**")
                
                // 허용할 오리진 (도메인)을 지정합니다.
                // 보안상 '*' 대신 실제 프론트엔드 도메인을 명시하는 것이 좋습니다.
                // 예: .allowedOrigins("http://localhost:3000", "https://your-frontend-domain.com")
                .allowedOrigins("*") 
                
                // 허용할 HTTP 메서드를 지정합니다.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                
                // 클라이언트에서 인증 정보 (쿠키, HTTP 인증 등)를 포함하여 요청할 수 있도록 허용합니다.
                // .allowedOrigins("*")를 사용할 경우, .allowCredentials(true)를 함께 사용할 수 없습니다. 
                // 특정 도메인만 허용할 때 사용해야 합니다.
                // .allowCredentials(true)
                
                // 허용할 헤더를 지정합니다.
                .allowedHeaders("*")
                
                // 캐싱 시간을 설정하여 Preflight 요청을 줄일 수 있습니다 (초 단위).
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드된 이미지 파일을 정적 리소스로 서빙
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }   
}
