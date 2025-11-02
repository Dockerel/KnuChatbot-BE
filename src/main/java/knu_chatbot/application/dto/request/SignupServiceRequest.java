package knu_chatbot.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupServiceRequest {

    private String email;
    private String password;
    private String confirmPassword;

    @Builder
    public SignupServiceRequest(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

}
