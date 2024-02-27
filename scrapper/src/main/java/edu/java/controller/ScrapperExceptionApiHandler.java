package edu.java.controller;

import edu.java.controller.dto.ApiErrorResponse;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkAlreadyRegisteredInChatException;
import edu.java.exception.LinkNotFoundByUrlException;
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

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> tgChatNotFound(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description("Tg chat was not found")
                .code("404")
                .exceptionName("ChatNotFoundException")
                .exceptionMessage("Tg chat was not found")
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(404)
        );
    }

    @ExceptionHandler(ChatAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> tgChatAlreadyExist(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description("Tg chat is already was registered")
                .code("404")
                .exceptionName("ChatAlreadyExistException")
                .exceptionMessage("Tg chat is already was registered")
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(404)
        );
    }

    @ExceptionHandler(LinkAlreadyRegisteredInChatException.class)
    public ResponseEntity<ApiErrorResponse> linkAlreadyRegistered(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description("Link was already tracked by chat")
                .code("404")
                .exceptionName("LinkAlreadyRegisteredInChatException")
                .exceptionMessage("Link was already tracked by chat")
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(404)
        );
    }
    @ExceptionHandler(LinkNotFoundByUrlException.class)
    public ResponseEntity<ApiErrorResponse> linkNotFoundByUrl(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description("Link was not found with such url")
                .code("404")
                .exceptionName("LinkNotFoundByUrlException")
                .exceptionMessage("Link was not found with such url")
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(404)
        );
    }
}
