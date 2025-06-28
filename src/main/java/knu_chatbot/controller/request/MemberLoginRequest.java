package knu_chatbot.controller.request;

import jakarta.validation.constraints.NotBlank;
import knu_chatbot.service.request.MemberLoginServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginRequest {

    @NotBlank(message = "유저 이메일은 필수입니다.")
    private String email;
    @NotBlank(message = "유저 비밀번호는 필수입니다.")
    private String password;

    @Builder
    public MemberLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberLoginServiceRequest toServiceRequest() {
        return MemberLoginServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
