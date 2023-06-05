package com.codestates.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenizerTest {
    private static JwtTokenizer tokenizer;
    private String secretKey;
    private String base64EncodedSecretKey;

    @BeforeAll
    public void init(){
        tokenizer = new JwtTokenizer();
        secretKey = "kevin1234123412341234123412341234";
        base64EncodedSecretKey = tokenizer.encodeBase64SecretKey(secretKey);
    }


    @Test
    @DisplayName("encoding 및 deconding 테스트")
    public void encodeBase64SecretKeyTest() {
        System.out.println(base64EncodedSecretKey);
        assertThat(secretKey).isEqualTo((new String(Decoders.BASE64.decode(base64EncodedSecretKey))));
    }

    @Test
    @DisplayName("access토큰은 null이 아니어야 함")
    public void generateAccessTokenTest() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expiration = calendar.getTime();

        String accessToken = tokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        System.out.println(accessToken);
        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("refresh토큰은 null이 아니어야 함")
    public void generateRefreshTokenTest() {
        String subject = "test refresh token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiration = calendar.getTime();

        String refreshToken = tokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        System.out.println(refreshToken);

        assertThat(refreshToken).isNotNull();
    }

    @Test
    @DisplayName("verify 기능에서 어떤 오류도 발생하지 않음")
    public void verifySignatureTest() {
        String accessToken = getAccessToken(Calendar.MINUTE, 10);
        assertThatCode(() -> {
            tokenizer.verifySignature(accessToken, base64EncodedSecretKey);
        }).doesNotThrowAnyException();
    }

    @DisplayName("throw ExpiredJwtException when jws verify")
    @Test
    public void verifyExpirationTest() throws InterruptedException {
        String accessToken = getAccessToken(Calendar.SECOND, 1);
        assertThatCode(() -> {
            tokenizer.verifySignature(accessToken, base64EncodedSecretKey);
        }).doesNotThrowAnyException();

        TimeUnit.MILLISECONDS.sleep(2000);

        assertThatThrownBy(() -> tokenizer.verifySignature(accessToken, base64EncodedSecretKey))
                .isInstanceOf(ExpiredJwtException.class);
    }

    private String getAccessToken(int timeUnit, int timeAmount) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", 1);
        claims.put("roles", List.of("USER"));

        String subject = "test access token";
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, timeAmount);
        Date expiration = calendar.getTime();
        String accessToken = tokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }
}