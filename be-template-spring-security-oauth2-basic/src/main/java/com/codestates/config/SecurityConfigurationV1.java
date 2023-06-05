package com.codestates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class SecurityConfigurationV1 {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http
//                .csrf().disable()
//                .formLogin().disable()
//                .httpBasic().disable() // http 기본 인증 비활성화
//                // 인증규칙 구성 : 모든 요청이 인증되어야 함
//                .authorizeHttpRequests( a -> a.anyRequest().authenticated())
//                // 기본 설정으로 OAuth2 로그인을 활성화
//                .oauth2Login(Customizer.withDefaults());
//        return http.build();
//    }
//}
