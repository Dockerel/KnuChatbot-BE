package knu_chatbot.controller.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionResponse {

    private String text;

    @Builder
    public QuestionResponse(String text) {
        this.text = text;
    }

    public static QuestionResponse of(String text) {
        return QuestionResponse.builder()
                .text(text)
                .build();
    }
}
