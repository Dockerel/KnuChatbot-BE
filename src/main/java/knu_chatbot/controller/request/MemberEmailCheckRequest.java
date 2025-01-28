package knu_chatbot.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberEmailCheckRequest {

    @NotBlank(message = "유저 이메일은 필수입니다.")
    private String email;

    @Builder
    public MemberEmailCheckRequest(String email) {
        this.email = email;
    }
}
