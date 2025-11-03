package knu_chatbot.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ChangePasswordResponse {

    private String message;

    @Builder
    public ChangePasswordResponse(String message) {
        this.message = message;
    }

    public static ChangePasswordResponse of(String message) {
        return ChangePasswordResponse.builder()
                .message(message)
                .build();
    }

}
