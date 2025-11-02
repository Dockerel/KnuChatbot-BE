package knu_chatbot.presentation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import knu_chatbot.application.dto.request.CheckEmailServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckEmailRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형태가 아닙니다.")
    private String email;

    @Builder
    public CheckEmailRequest(String email) {
        this.email = email;
    }

    public CheckEmailServiceRequest toServiceRequest() {
        return CheckEmailServiceRequest.builder()
                .email(email)
                .build();
    }

}
