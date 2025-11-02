package knu_chatbot.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import knu_chatbot.application.dto.request.SignupServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형태가 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Length(min = 8, max = 20, message = "길이는 최소 8글자, 최대 20글자 입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    @Builder
    public SignupRequest(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public SignupServiceRequest toServiceRequest() {
        return SignupServiceRequest.builder()
                .email(email)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
    }
}
