package knu_chatbot.application.error;

import lombok.Getter;

@Getter
public class KnuChatbotException extends RuntimeException {

    private final ErrorType errorType;
    private final Object data;

    public KnuChatbotException(ErrorType errorType, Object data) {
        this.errorType = errorType;
        this.data = data;
    }

    public KnuChatbotException(ErrorType errorType) {
        this.errorType = errorType;
        this.data = null;
    }
}
