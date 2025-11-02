package knu_chatbot.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckEmailResponse {

    boolean valid;

    @Builder
    public CheckEmailResponse(boolean valid) {
        this.valid = valid;
    }

    public static CheckEmailResponse of(boolean result) {
        return CheckEmailResponse.builder()
                .valid(result)
                .build();
    }
}
