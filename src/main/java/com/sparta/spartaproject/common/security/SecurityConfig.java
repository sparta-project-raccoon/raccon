package com.sparta.spartaproject.common.security;

import com.sparta.spartaproject.domain.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // basic auth 및 csrf 보안을 사용하지 않음
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)

            // 토큰 사용으로 세션 사용 X
            .sessionManagement(
                sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(
                authorize -> authorize
                    .requestMatchers("/api/users/signup").permitAll()
                    .requestMatchers("/api/users/login").permitAll()
                    .requestMatchers("/api/users/find-username").permitAll()
                    .requestMatchers("/api/users/find-password").permitAll()
                    .anyRequest().authenticated()
            )

            // 직접 구현한 필터 적용하기
            .addFilterBefore(
                new JwtAuthFilter(jwtUtils, customUserDetailsService),
                UsernamePasswordAuthenticationFilter.class
            )

            .build();
    }
}