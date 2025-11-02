package knu_chatbot.application.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class MemberSignupServiceRequest {

    private String email;
    private String password;
    private String passwordCheck;
    private String nickname;
}
