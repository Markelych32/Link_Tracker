package edu.java.exception;

import edu.java.controller.dto.response.ApiErrorResponse;
import java.util.List;
import org.springframework.http.HttpStatus;

public interface ScrapperException {
    String description();

    default String code() {
        return String.valueOf(HttpStatus.NOT_FOUND.value());
    }

    default String exceptionName() {
        return getClass().getName();
    }

    String exceptionMessage();

    default ApiErrorResponse exceptionToApiErrorResponse(List<String> stacktrace) {
        return ApiErrorResponse.builder()
            .description(description())
            .code(code())
            .exceptionName(exceptionName())
            .exceptionMessage(exceptionMessage())
            .stacktrace(stacktrace)
            .build();
    }
}
