// SecurityConfig.java の修正
package com.terragrouplabs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // 静的リソースとログインページをすべて公開
                .requestMatchers(
                    "/", "/index", "/about", "/service", 
                    "/contact/**", "/thankyou", "/css/**", 
                    "/js/**", "/images/**", "/login", "/error"
                ).permitAll()
                // 管理者ページだけ認証必要
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // その他のリクエストはすべて許可
                .anyRequest().permitAll()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin/messages", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .headers((headers) -> headers
                .contentSecurityPolicy((csp) -> csp
                    .policyDirectives("default-src 'self'; " +
                        "script-src 'self' https://www.google.com https://www.gstatic.com https://cdn.jsdelivr.net 'unsafe-inline'; " +
                        "style-src 'self' https://fonts.googleapis.com https://cdn.jsdelivr.net 'unsafe-inline'; " +
                        "img-src 'self' data:; " +
                        "font-src 'self' https://fonts.gstatic.com https://cdn.jsdelivr.net; " +
                        "frame-src 'self' https://www.google.com")
                )
                .xssProtection((xss) -> xss
                    .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                )
                .contentTypeOptions(contentTypeOptions -> {})
                .frameOptions(frameOptions -> frameOptions.deny())
            );
        
        return http.build();
    }
    
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = org.springframework.security.core.userdetails.User
            .withUsername("admin")
            .password(passwordEncoder.encode("password")) // 本番環境では設定ファイルから読み込むようにする
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }
}