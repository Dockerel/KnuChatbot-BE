package knu_chatbot.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupResponse {

    private String message;

    @Builder
    public SignupResponse(String message) {
        this.message = message;
    }

    public static SignupResponse of(String message) {
        return SignupResponse.builder()
                .message(message)
                .build();
    }
}
