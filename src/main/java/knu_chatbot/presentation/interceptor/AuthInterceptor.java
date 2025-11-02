package knu_chatbot.presentation.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.application.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static knu_chatbot.application.util.AuthConst.*;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(AUTHORIZATION);

        if (isInvalidAuthorizationHeaderFormat(header)) {
            throw new KnuChatbotException(ErrorType.USER_INVALID_ACCESS_TOKEN_ERROR);
        }

        String token = header.substring(BEARER.length());

        AuthUser authUser = getAuthenticatedUser(token);

        request.setAttribute(AUTHENTICATED_USER, authUser);

        return true;
    }

    private boolean isInvalidAuthorizationHeaderFormat(String header) {
        return header == null || !header.startsWith(BEARER);
    }

    private AuthUser getAuthenticatedUser(String token) {
        Claims claims = jwtProvider.getClaimsFromAccessToken(token);
        if (claims == null) {
            throw new KnuChatbotException(ErrorType.USER_INVALID_ACCESS_TOKEN_ERROR);
        }

        String authenticatedUserString = claims.get(AUTHENTICATED_USER).toString();

        try {
            AuthUser authUser = objectMapper.readValue(authenticatedUserString, AuthUser.class);
            return authUser;
        } catch (JsonProcessingException e) {
            throw new KnuChatbotException(ErrorType.DEFAULT_ERROR);
        }
    }

}
