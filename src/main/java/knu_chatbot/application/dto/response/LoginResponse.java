package knu_chatbot.application.dto.response;

import knu_chatbot.presentation.response.AccessTokenResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse of(String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AccessTokenResponse toAccessTokenResponse() {
        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
