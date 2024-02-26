package edu.java.bot.controller;

import edu.java.bot.controller.dto.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class BotExceptionApiHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> notValidLinkUpdate(MethodArgumentNotValidException exception) {
        List<String> test = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description("ID, URL & tgChatIds cant be empty")
                .code("400")
                .exceptionName("HttpMessageNotReadableException")
                .exceptionMessage("ID, URL & tgChatIds cant be empty")
                .stacktrace(test)
                .build(), HttpStatusCode.valueOf(404)
        );
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> notIntId(HttpMessageNotReadableException exception) {
        List<String> test = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description("ID should be INT")
                .code("404")
                .exceptionName("HttpMessageNotReadableException")
                .exceptionMessage("ID should be INT")
                .stacktrace(test)
                .build(), HttpStatusCode.valueOf(404)
        );
    }
}
