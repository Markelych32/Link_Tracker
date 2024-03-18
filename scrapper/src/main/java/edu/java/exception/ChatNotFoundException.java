package edu.java.exception;

public class ChatNotFoundException extends RuntimeException implements ScrapperException {

    private static final String EXCEPTION_DESCRIPTION = "You are trying to access a non-existent chat";
    private static final String EXCEPTION_MESSAGE = "Chat not found";

    @Override
    public String description() {
        return EXCEPTION_DESCRIPTION;
    }

    @Override
    public String exceptionMessage() {
        return EXCEPTION_MESSAGE;
    }
}
