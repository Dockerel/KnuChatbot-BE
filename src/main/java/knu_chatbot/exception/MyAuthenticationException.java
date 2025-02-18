package knu_chatbot.exception;

public class MyAuthenticationException extends RuntimeException {

    public MyAuthenticationException(String message) {
        super(message);
    }

    public MyAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
