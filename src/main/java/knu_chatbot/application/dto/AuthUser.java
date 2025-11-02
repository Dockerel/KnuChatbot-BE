package knu_chatbot.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthUser {

    private String email;

    @Builder
    public AuthUser(String email) {
        this.email = email;
    }

}
