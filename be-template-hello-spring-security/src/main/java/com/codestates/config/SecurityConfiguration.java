package com.codestates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    // Spring Security의 설정 진행
//    @Bean
//    public UserDetailsManager userDetailsManager(){
//        // UserDetails 고정으로 생성
//        UserDetails userDetails = User.withDefaultPasswordEncoder() // 고정해서 사용하지 말라는 Deprecated
//                .username("user@naver.com")
//                .password("123123")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin@naver.com")
//                .password("123123")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails, admin);
//    }
    // SecurityFilterChain을 Bean으로 등록해서 HTTP 보안 설정을 구성하는 방식을 권장
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // HttpSecurity를 통해 HTTP 요청에 대한 보안 설정을 구성한다.
        http
                .headers().frameOptions().sameOrigin() // H2 웹 콘솔을 정상적으로 사용하기 위한 설정
                .and()
                .csrf().disable()
                .formLogin()  // 폼 로그인 방식 지정
                .loginPage("/auths/login-form") // 커스텀 로그인 페이지 사용
                .loginProcessingUrl("/process_login") // 요청 URL 설정
                .failureUrl("/auths/login-form?error")
                .and() // 체인 형태로 구성
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedPage("/auths/access-denied") // 권한 없는 사용자 처리
                .and()
                .authorizeHttpRequests(authorize -> authorize
                                .antMatchers("/orders/**").hasRole("ADMIN") // 관리자 권한
                                .antMatchers("/members/my-page").hasRole("USER")   // user 권한
                                .antMatchers("⁄**").permitAll()                    // ETC
                );
                /**
                 .authorizeHttpRequests() // 요청이 오면 접근 권한 확인
                 .anyRequest()
                 .permitAll(); // 어떤 요청이든 모든 권한 허용
                 */


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
