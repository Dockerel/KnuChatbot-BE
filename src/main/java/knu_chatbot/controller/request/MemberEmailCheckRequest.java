package knu_chatbot.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import knu_chatbot.service.request.MemberEmailCheckServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberEmailCheckRequest {

    @NotBlank(message = "유저 이메일은 필수입니다.")
    @Email(message = "이메일 형태로 입력해주세요.")
    private String email;

    @Builder
    public MemberEmailCheckRequest(String email) {
        this.email = email;
    }

    public MemberEmailCheckServiceRequest toServiceRequest() {
        return MemberEmailCheckServiceRequest.builder()
            .email(email)
            .build();
    }
}
