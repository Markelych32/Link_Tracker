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

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorResponse> notIntId(Exception exception) {
        List<String> test = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description("Unadjusted request parameters")
                .code("404")
                .exceptionName("HttpMessageNotReadableException, MethodArgumentNotValidException")
                .exceptionMessage("Unadjusted request parameters")
                .stacktrace(test)
                .build(), HttpStatusCode.valueOf(404)
        );
    }
}
