package knu_chatbot.application.service.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberEmailCheckServiceRequest {

    private String email;

    @Builder
    public MemberEmailCheckServiceRequest(String email) {
        this.email = email;
    }
}
