package knu_chatbot.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WithdrawResponse {

    private String message;

    @Builder
    public WithdrawResponse(String message) {
        this.message = message;
    }

    public static WithdrawResponse of(String message) {
        return WithdrawResponse.builder()
                .message(message)
                .build();
    }

}
