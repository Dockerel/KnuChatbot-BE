package knu_chatbot.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangePasswordServiceRequest {

    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    @Builder
    public ChangePasswordServiceRequest(String oldPassword, String newPassword, String confirmNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

}
