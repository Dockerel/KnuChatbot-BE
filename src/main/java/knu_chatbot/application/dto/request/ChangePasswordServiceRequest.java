package knu_chatbot.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChangePasswordServiceRequest {

    private String oldPassword;
    private String newPassword;

    @Builder
    public ChangePasswordServiceRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

}
