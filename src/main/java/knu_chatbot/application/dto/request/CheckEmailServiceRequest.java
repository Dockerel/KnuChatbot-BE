package knu_chatbot.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CheckEmailServiceRequest {

    private String email;

    @Builder
    public CheckEmailServiceRequest(String email) {
        this.email = email;
    }
    
}
