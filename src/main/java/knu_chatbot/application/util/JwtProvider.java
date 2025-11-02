package knu_chatbot.application.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    public static final long ACCESS_TOKEN_EXPIRE_SECONDS = 60 * 60;
    public static final long REFRESH_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24 * 14;

    private final ObjectMapper objectMapper;

    @Value("${jwt.secret}")
    private String secretKey;

    private byte[] secretKeyBytes;
    private Key key;

    @PostConstruct
    public void init() {
        secretKeyBytes = secretKey.getBytes();
        key = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String createAccessToken(String email) {
        Map<String, Object> claims = createClaims(email);

        Date expireDate = getExpireDateAccessToken();

        return Jwts.builder()
                .claims(claims)
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String email) {
        Map<String, Object> claims = createClaims(email);

        Date expireDate = getExpireDateRefreshToken();

        return Jwts.builder()
                .claims(claims)
                .expiration(expireDate)
                .signWith(key)
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parse(token);
        } catch (Exception e) {
            throw new KnuChatbotException(ErrorType.USER_INVALID_REFRESH_TOKEN_ERROR);
        }
    }

    public Claims getClaimsFromAccessToken(String token) {
        try {
            return (Claims) Jwts.parser()
                    .verifyWith((SecretKey) key)
                    .build()
                    .parse(token)
                    .getPayload();
        } catch (Exception e) {
            throw new KnuChatbotException(ErrorType.USER_INVALID_ACCESS_TOKEN_ERROR);
        }
    }

    private Map<String, Object> createClaims(String email) {
        Map<String, Object> claims = new HashMap<>();

        AuthUser authUser = new AuthUser(email);

        try {
            String authenticateUserString = objectMapper.writeValueAsString(authUser);
            claims.put(AuthConst.AUTHENTICATED_USER, authenticateUserString);
        } catch (IOException e) {
            throw new KnuChatbotException(ErrorType.DEFAULT_ERROR);
        }

        return claims;
    }

    private Date getExpireDateAccessToken() { // 1시간
        long expireTimeMills = 1000L * ACCESS_TOKEN_EXPIRE_SECONDS;
        return new Date(System.currentTimeMillis() + expireTimeMills);
    }

    private Date getExpireDateRefreshToken() { // 14일
        long expireTimeMills = 1000L * REFRESH_TOKEN_EXPIRE_SECONDS;
        return new Date(System.currentTimeMillis() + expireTimeMills);
    }
}
