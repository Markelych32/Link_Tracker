package edu.java.controller;

import edu.java.controller.dto.ApiErrorResponse;
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
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ScrapperExceptionApiHandler {

    private static final String UNADJUSTED_REQUEST_PARAMETERS = "Unadjusted request parameters";
    private static final String TG_CHAT_NOT_FOUND = "Tg chat was not found";
    private static final String CHAT_ALREADY_EXIST = "Tg chat is already was registered";
    private static final String LINK_ALREADY_TRACKED = "Link was already tracked by chat";

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
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

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> tgChatNotFound(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description(TG_CHAT_NOT_FOUND)
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .exceptionName("ChatNotFoundException")
                .exceptionMessage(TG_CHAT_NOT_FOUND)
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value())
        );
    }

    @ExceptionHandler(ChatAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> tgChatAlreadyExist(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description(CHAT_ALREADY_EXIST)
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .exceptionName("ChatAlreadyExistException")
                .exceptionMessage(CHAT_ALREADY_EXIST)
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value())
        );
    }

    @ExceptionHandler(LinkAlreadyRegisteredInChatException.class)
    public ResponseEntity<ApiErrorResponse> linkAlreadyRegistered(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description(LINK_ALREADY_TRACKED)
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .exceptionName("LinkAlreadyRegisteredInChatException")
                .exceptionMessage(LINK_ALREADY_TRACKED)
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value())
        );
    }

    @ExceptionHandler(LinkNotFoundByUrlException.class)
    public ResponseEntity<ApiErrorResponse> linkNotFoundByUrl(Exception exception) {
        List<String> list = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();
        return new ResponseEntity<>(
            ApiErrorResponse.builder()
                .description(LINK_ALREADY_TRACKED)
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .exceptionName("LinkNotFoundByUrlException")
                .exceptionMessage(LINK_ALREADY_TRACKED)
                .stacktrace(list)
                .build(), HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value())
        );
    }
}
