package knu_chatbot.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDto {

    private String email;
    private String password;

    @Builder
    public MemberDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
