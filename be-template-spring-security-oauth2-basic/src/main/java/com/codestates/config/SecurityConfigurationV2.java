package com.codestates.config;

import com.codestates.member.service.MemberService;
import com.codestates.oauth2_jwt.filter.JwtVerificationFilter;
import com.codestates.oauth2_jwt.handler.MemberAccessDeniedHandler;
import com.codestates.oauth2_jwt.handler.MemberAuthenticationEntryPoint;
import com.codestates.oauth2_jwt.handler.OAuth2MemberSuccessHandler;
import com.codestates.oauth2_jwt.jwt.JwtTokenizer;
import com.codestates.oauth2_jwt.utils.CustomAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;


//public class SecurityConfigurationV2 {
//    @Value("${spring.security.oauth2.client.registration.google.clientId}")
//    private String clientId;
//    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
//    private String clientSecret;
//
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
//
//    /**
//     *  ClientRegistration을 저장하기 위한 Repository
//     */
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository(){
//        var clientRegistration = clientRegistration();
//
//        return new InMemoryClientRegistrationRepository(clientRegistration);
//    }
//
//    private ClientRegistration clientRegistration(){
//        return CommonOAuth2Provider
//                .GOOGLE
//                .getBuilder("google")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .build();
//    }
//}
@Configuration
public class SecurityConfigurationV2 {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;

    public SecurityConfigurationV2(JwtTokenizer jwtTokenizer,
                                 CustomAuthorityUtils authorityUtils,
                                 MemberService memberService) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberService = memberService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()  // 추가
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())  // 추가
                .accessDeniedHandler(new MemberAccessDeniedHandler())            // 추가
                .and()
                .apply(new CustomFilterConfigurer())  // 추가
                .and()
                .authorizeHttpRequests(authorize -> authorize // url authorization 전체 추가
//                        .antMatchers(HttpMethod.POST, "/*/members").permitAll()    // OAuth 2로 로그인하므로 회원 정보 등록 필요 없음.
//                        .antMatchers(HttpMethod.PATCH, "/*/members/**").hasRole("USER") // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
//                        .antMatchers(HttpMethod.GET, "/*/members").hasRole("ADMIN")  // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
//                        .antMatchers(HttpMethod.GET, "/*/members/**").hasAnyRole("USER", "ADMIN")  // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
//                        .antMatchers(HttpMethod.DELETE, "/*/members/**").hasRole("USER") // OAuth 2로 로그인하므로 회원 정보 수정 필요 없음.
                                .antMatchers(HttpMethod.POST, "/*/coffees").hasRole("ADMIN")
                                .antMatchers(HttpMethod.PATCH, "/*/coffees/**").hasRole("ADMIN")
                                .antMatchers(HttpMethod.GET, "/*/coffees/**").hasAnyRole("USER", "ADMIN")
                                .antMatchers(HttpMethod.GET, "/*/coffees").permitAll()
                                .antMatchers(HttpMethod.DELETE, "/*/coffees").hasRole("ADMIN")
                                .antMatchers(HttpMethod.POST, "/*/orders").hasRole("USER")
                                .antMatchers(HttpMethod.PATCH, "/*/orders").hasAnyRole("USER", "ADMIN")
                                .antMatchers(HttpMethod.GET, "/*/orders/**").hasAnyRole("USER", "ADMIN")
                                .antMatchers(HttpMethod.DELETE, "/*/orders").hasRole("USER")
                                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2MemberSuccessHandler(jwtTokenizer, authorityUtils, memberService))  // (1)
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 주의 사항: 컨텐츠 표시 오류로 인해 '/**'를 '\/**'로 표기했으니 실제 코드 구현 시에는 '\(역슬래시)'를 빼 주세요.

        return source;
    }

    // 추가
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            builder.addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class); // (2)
        }
    }
}