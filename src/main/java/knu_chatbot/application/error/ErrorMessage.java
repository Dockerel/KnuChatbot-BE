package knu_chatbot.application.error;

import lombok.Getter;

@Getter
public class ErrorMessage {
    private String message;
    private String code;
    private Object data;

    public ErrorMessage(ErrorType errorType, Object data) {
        this.message = errorType.getMessage();
        this.code = errorType.getStatus().name();
        this.data = data;
    }

    public ErrorMessage(ErrorType errorType) {
        this.message = errorType.getMessage();
        this.code = errorType.getStatus().name();
        this.data = null;
    }
}
