package com.codestates.hello_oauth2.home;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloHomeController {
    private final OAuth2AuthorizedClientService authorizedClientService;

    public HelloHomeController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/V1")
    public String home(){
        return "hello-oauth2";
    }

    @GetMapping("/V2")
    public String home2(){
        var oAuth2User = (OAuth2User)SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal(); // 인증된 Authentication 객체에서 Principal(계정정보)을 얻음
        System.out.println(oAuth2User.getAttributes().get("email"));
        return "hello-oauth2";
    }

    @GetMapping("/V3")
    public String home(Authentication authentication) {    // (1)
        var oAuth2User = (OAuth2User)authentication.getPrincipal();
        System.out.println(oAuth2User);
        System.out.println("User's email in Google: " + oAuth2User.getAttributes().get("email"));

        return "hello-oauth2";
    }

    @GetMapping("/V4")
    public String home(@AuthenticationPrincipal OAuth2User oAuth2User) {  // (1)
        System.out.println("User's email in Google: " + oAuth2User.getAttributes().get("email"));

        return "hello-oauth2";
    }

    @GetMapping("/V5")
    public String home5(Authentication authentication) {
        var authorizedClient = authorizedClientService.loadAuthorizedClient("google", authentication.getName()); // (2)

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println("Access Token Value: " + accessToken.getTokenValue());  // Access Token의 문자열
        System.out.println("Access Token Type: " + accessToken.getTokenType().getValue());  // Token type
        System.out.println("Access Token Scopes: " + accessToken.getScopes());       // 토큰으로 접근 가능한 Scope
        System.out.println("Access Token Issued At: " + accessToken.getIssuedAt());    // 발행일시
        System.out.println("Access Token Expires At: " + accessToken.getExpiresAt());  // 만료일시

        return "hello-oauth2";
    }

    @GetMapping("/V6")
    public String home(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
        System.out.println("Access Token Value: " + accessToken.getTokenValue());
        System.out.println("Access Token Type: " + accessToken.getTokenType().getValue());
        System.out.println("Access Token Scopes: " + accessToken.getScopes());
        System.out.println("Access Token Issued At: " + accessToken.getIssuedAt());
        System.out.println("Access Token Expires At: " + accessToken.getExpiresAt());

        return "hello-oauth2";
    }

}
