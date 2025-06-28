package knu_chatbot.controller.response;


import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionAndAnswerResponse {
    private QuestionResponse questionResponse;
    private AnswerResponse answerResponse;

    @Builder
    public QuestionAndAnswerResponse(QuestionResponse questionResponse, AnswerResponse answerResponse) {
        this.questionResponse = questionResponse;
        this.answerResponse = answerResponse;
    }

    public static QuestionAndAnswerResponse of(QuestionResponse questionResponse, AnswerResponse answerResponse) {
        return QuestionAndAnswerResponse.builder()
                .questionResponse(questionResponse)
                .answerResponse(answerResponse)
                .build();
    }
}

