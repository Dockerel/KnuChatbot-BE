package knu_chatbot.application.dto.response;

import knu_chatbot.presentation.response.AccessTokenResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReissueTokensResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    public ReissueTokensResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ReissueTokensResponse of(String accessToken, String refreshToken) {
        return ReissueTokensResponse.builder()
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
