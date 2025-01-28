package knu_chatbot.service.request;

import knu_chatbot.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSignupServiceRequest {

    private String email;
    private String password;
    private String nickname;

    public Member toEntity(String encryptPassword) {
        return Member.builder()
            .email(email)
            .password(encryptPassword)
            .nickname(nickname)
            .build();
    }

    @Builder
    public MemberSignupServiceRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
