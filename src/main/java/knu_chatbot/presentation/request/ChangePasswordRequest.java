package knu_chatbot.presentation.request;

import jakarta.validation.constraints.NotBlank;
import knu_chatbot.application.dto.response.ChangePasswordServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "이전 비밀번호는 필수입니다.")
    private String oldPassword;

    @NotBlank(message = "새로운 비밀번호는 필수입니다.")
    @Length(min = 8, max = 20, message = "길이는 최소 8글자, 최대 20글자 입니다.")
    private String newPassword;

    @Builder
    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public ChangePasswordServiceRequest toServiceRequest() {
        return ChangePasswordServiceRequest.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();
    }
}
