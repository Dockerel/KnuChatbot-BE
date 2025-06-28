package knu_chatbot.controller.response;

import knu_chatbot.entity.Answer;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class AnswerResponse {

    private String text;
    private String references;
    private List<String> images;

    @Builder
    public AnswerResponse(String text, String references, List<String> images) {
        this.text = text;
        this.references = references;
        this.images = images;
    }

    public static AnswerResponse of(String text, String references, List<String> images) {
        return AnswerResponse.builder()
                .text(text)
                .references(references)
                .images(images)
                .build();
    }

    public static AnswerResponse of(Answer answer, List<String> images) {
        return of(answer.getText(), answer.getReferences(), images);
    }
}
