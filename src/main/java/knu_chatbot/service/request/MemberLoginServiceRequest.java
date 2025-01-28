package knu_chatbot.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginServiceRequest {

    private String email;
    private String password;

    @Builder
    public MemberLoginServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
