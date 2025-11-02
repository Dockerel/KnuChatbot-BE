package knu_chatbot.application.util;

import knu_chatbot.application.error.ErrorType;
import knu_chatbot.application.error.KnuChatbotException;
import knu_chatbot.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JwtProviderTest extends IntegrationTestSupport {

    @Autowired
    private JwtProvider jwtProvider;

    @DisplayName("accessToken을 생성한다.")
    @Test
    void createAccessToken() {
        // given
        String email = "test@test.com";

        // when
        String accessToken = jwtProvider.createAccessToken(email);

        // then
        assertThat(accessToken).isNotNull();
    }

    @DisplayName("refreshToken을 생성한다.")
    @Test
    void createRefreshToken() {
        // given
        String email = "test@test.com";

        // when
        String refreshToken = jwtProvider.createRefreshToken(email);

        // then
        assertThat(refreshToken).isNotNull();
    }

    @DisplayName("유효한 토큰을 검증한다.")
    @Test
    void validateValidRefreshToken() {
        // given
        String email = "test@test.com";
        String refreshToken = jwtProvider.createRefreshToken(email);

        // when // then
        assertDoesNotThrow(() -> jwtProvider.validateToken(refreshToken));
    }

    @DisplayName("유효하지 않은 토큰을 검증한다.")
    @Test
    void validateInvalidRefreshToken() {
        // given
        String refreshToken = "invalid.refresh.token";

        // when // then
        assertThatThrownBy(() -> jwtProvider.validateToken(refreshToken))
                .isInstanceOf(KnuChatbotException.class)
                .extracting("ErrorType")
                .isEqualTo(ErrorType.USER_INVALID_REFRESH_TOKEN_ERROR);
    }

}