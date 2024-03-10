package edu.java.exception;

public class LinkAlreadyRegisteredInChatException extends RuntimeException implements ScrapperException {

    private static final String EXCEPTION_DESCRIPTION = "You are trying to add already tracking link by chat";
    private static final String EXCEPTION_MESSAGE = "Link is already tracked";

    @Override
    public String description() {
        return EXCEPTION_DESCRIPTION;
    }

    @Override
    public String exceptionMessage() {
        return EXCEPTION_MESSAGE;
    }
}
