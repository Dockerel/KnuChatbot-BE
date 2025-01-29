package knu_chatbot.controller.request;

import jakarta.validation.constraints.NotBlank;
import knu_chatbot.service.request.MemberSignupServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignupRequest {

    @NotBlank(message = "유저 이메일은 필수입니다.")
    private String email;
    @NotBlank(message = "유저 비밀번호는 필수입니다.")
    private String password;
    @NotBlank(message = "유저 닉네임은 필수입니다.")
    private String nickname;

    public MemberSignupServiceRequest toServiceRequest() {
        return MemberSignupServiceRequest.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }

    @Builder
    public MemberSignupRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
