package edu.java.controller;

import edu.java.controller.dto.response.ApiErrorResponse;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ScrapperExceptionApiHandler {

    private static final String UNADJUSTED_REQUEST_PARAMETERS = "Unadjusted request parameters";

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class,
        HandlerMethodValidationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> notCorrectTgChatRequest(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description(UNADJUSTED_REQUEST_PARAMETERS)
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .exceptionName("MethodArgumentTypeMismatchException, MethodArgumentNotValidException")
                .exceptionMessage(UNADJUSTED_REQUEST_PARAMETERS)
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse tgChatNotFound(ChatNotFoundException exception) {
        return exception.exceptionToApiErrorResponse(Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString).toList());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse tgChatAlreadyExist(ChatAlreadyExistException exception) {
        return exception.exceptionToApiErrorResponse(Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString).toList());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse linkAlreadyRegistered(LinkAlreadyRegisteredInChatException exception) {
        return exception.exceptionToApiErrorResponse(Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString).toList());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse linkNotFoundByUrl(LinkNotFoundByUrlException exception) {
        return exception.exceptionToApiErrorResponse(Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString).toList());
    }
}
