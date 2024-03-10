package edu.java.exception;

public class ChatAlreadyExistException extends RuntimeException implements ScrapperException {

    private static final String EXCEPTION_DESCRIPTION = "You are trying to register chat which already exists";
    private static final String EXCEPTION_MESSAGE = "Chat already exists";

    @Override
    public String description() {
        return EXCEPTION_DESCRIPTION;
    }

    @Override
    public String exceptionMessage() {
        return EXCEPTION_MESSAGE;
    }
}
