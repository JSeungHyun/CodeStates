//package com.codestates.auth;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.Optional;
//
//@Component
//public class HelloUserAuthenticationProviderV2 implements AuthenticationProvider {
//    private final HelloUserDetailsServiceV3 userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    public HelloUserAuthenticationProviderV2(HelloUserDetailsServiceV3 userDetailsService, PasswordEncoder passwordEncoder) {
//        this.userDetailsService = userDetailsService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    // V1에서 AuthenticationException 수정
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
//
//        String username = authToken.getName();
//        Optional.ofNullable(username).orElseThrow(() -> new UsernameNotFoundException("Invalid User name or User Password"));
//        try {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            String password = userDetails.getPassword();
//            verifyCredentials(authToken.getCredentials(), password);
//
//            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
//            return UsernamePasswordAuthenticationToken.authenticated(username, password, authorities);
//        }catch (Exception e){
//            throw new UsernameNotFoundException(e.getMessage());
//        }
//    }
//
//    /*
//    supports 메서드는 Custom AuthenticationProvider로 Username/password 방식의 인증을 지원한다는 것을
//    Spring Security에 알려주는 역할
//     */
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.equals(authentication);
//    }
//
//    private void verifyCredentials(Object credentials, String password) {
//        if (!passwordEncoder.matches((String)credentials, password)) {
//            throw new BadCredentialsException("Invalid User name or User Password");
//        }
//    }
//}
