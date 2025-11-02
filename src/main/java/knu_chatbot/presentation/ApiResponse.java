package knu_chatbot.presentation;

import knu_chatbot.application.error.ErrorMessage;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private ResultType resultType;
    private T data;
    private ErrorMessage errorMessage;

    public ApiResponse(ResultType resultType, T data, ErrorMessage errorMessage) {
        this.resultType = resultType;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorMessage errorMessage) {
        return new ApiResponse<>(ResultType.ERROR, null, errorMessage);
    }
}
