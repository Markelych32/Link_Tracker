package edu.java.exception;

public class LinkNotFoundByUrlException extends RuntimeException implements ScrapperException {

    private static final String EXCEPTION_DESCRIPTION = "This link has not yet been added to the chat";
    private static final String EXCEPTION_MESSAGE = "Link not found";

    @Override
    public String description() {
        return EXCEPTION_DESCRIPTION;
    }

    @Override
    public String exceptionMessage() {
        return EXCEPTION_MESSAGE;
    }
}
