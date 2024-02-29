package edu.java.bot.controller;

import edu.java.bot.controller.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BotExceptionApiHandler {

    private static final String UNADJUSTED_REQUEST_PARAMETERS = "Unadjusted request parameters";

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorResponse> notCorrectLinkUpdate(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description(UNADJUSTED_REQUEST_PARAMETERS)
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .exceptionName("HttpMessageNotReadableException, MethodArgumentNotValidException")
                .exceptionMessage(UNADJUSTED_REQUEST_PARAMETERS)
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())
        );
    }
}
