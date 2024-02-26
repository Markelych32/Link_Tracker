package edu.java.controller;

import edu.java.controller.dto.ApiErrorResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ScrapperExceptionApiHandler {

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorResponse> notCorrectTgChatRequest(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description("Unadjusted request parameters")
                .code("400")
                .exceptionName("MethodArgumentTypeMismatchException, MethodArgumentNotValidException")
                .exceptionMessage("Unadjusted request parameters")
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(400)
        );
    }
}
