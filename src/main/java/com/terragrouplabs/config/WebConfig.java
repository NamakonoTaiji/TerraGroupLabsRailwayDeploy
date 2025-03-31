package com.terragrouplabs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                                  Object handler, ModelAndView modelAndView) {
                // 基本的なセキュリティヘッダー
                response.setHeader("X-Content-Type-Options", "nosniff");
                response.setHeader("X-Frame-Options", "DENY");
                response.setHeader("X-XSS-Protection", "1; mode=block");
                response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
                response.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
                
                // 開発環境と本番環境で異なるCSP設定を使用
                if ("dev".equals(activeProfile)) {
                    // 開発環境向けの緩和されたCSP設定
                    response.setHeader("Content-Security-Policy", 
                        "default-src 'self'; " +
                        "script-src 'self' https: 'unsafe-inline'; " +
                        "style-src 'self' https: 'unsafe-inline'; " +
                        "img-src 'self' data: https:; " +
                        "font-src 'self' data: https:; " +
                        "frame-src 'self' https:;");
                } else {
                    // 本番環境向けの厳格なCSP設定
                    response.setHeader("Content-Security-Policy", 
                        "default-src 'self'; " +
                        "script-src 'self' https://cdn.jsdelivr.net https://www.google.com https://www.gstatic.com; " +
                        "style-src 'self' https://cdn.jsdelivr.net https://fonts.googleapis.com 'unsafe-inline'; " +
                        "img-src 'self' data: https://www.google.com https://www.gstatic.com; " +
                        "font-src 'self' https://cdn.jsdelivr.net https://fonts.gstatic.com data:; " +
                        "frame-src 'self' https://www.google.com https://recaptcha.google.com; " +
                        "connect-src 'self'; " +
                        "base-uri 'self';");
                }
            }
        });
    }
}