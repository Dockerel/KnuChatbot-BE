package knu_chatbot.controller.advice;

import knu_chatbot.controller.response.ApiResponse;
import knu_chatbot.exception.MyAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyAuthenticationException.class)
    public ApiResponse<Object> myAuthenticationException(MyAuthenticationException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getMessage(),
            null
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            null
        );
    }

}
