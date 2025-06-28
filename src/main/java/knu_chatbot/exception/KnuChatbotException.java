package knu_chatbot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KnuChatbotException extends RuntimeException {

    private final HttpStatus httpStatus;

    public KnuChatbotException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public KnuChatbotException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
