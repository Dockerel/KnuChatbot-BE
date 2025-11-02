package knu_chatbot.presentation.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import knu_chatbot.application.dto.AuthUser;
import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.application.util.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthInterceptor authInterceptor;

    @DisplayName("유효한 JWT 토큰이면 true를 요청을 통과시킨다.")
    @Test
    void preHandle() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        given(request.getHeader(anyString()))
                .willReturn("Bearer accessToken");

        Claims claims = Mockito.mock(Claims.class);
        given(jwtProvider.getClaimsFromAccessToken(anyString()))
                .willReturn(claims);

        given(claims.get(anyString()))
                .willReturn("test");

        AuthUser authUser = Mockito.mock(AuthUser.class);
        given(objectMapper.readValue(anyString(), eq(AuthUser.class)))
                .willReturn(authUser);

        // when
        boolean result = authInterceptor.preHandle(request, null, null);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("Authorization이 없는 요청은 통과시키지 않는다.")
    @Test
    void preHandleWithoutAuthorizationHeader() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        // when // then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, null, null))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_INVALID_ACCESS_TOKEN_ERROR);
    }

    @DisplayName("Bearer 토큰이 아닌 요청은 통과시키지 않는다.")
    @Test
    void preHandleWithoutBearerToken() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        given(request.getHeader(anyString()))
                .willReturn("accessToken");

        // when // then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, null, null))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_INVALID_ACCESS_TOKEN_ERROR);
    }

    @DisplayName("Claim이 없는 요청은 통과시키지 않는다.")
    @Test
    void preHandleWithoutClaim() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        given(request.getHeader(anyString()))
                .willReturn("Bearer accessToken");

        given(jwtProvider.getClaimsFromAccessToken(anyString()))
                .willReturn(null);

        // when // then
        assertThatThrownBy(() -> authInterceptor.preHandle(request, null, null))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_INVALID_ACCESS_TOKEN_ERROR);
    }

}