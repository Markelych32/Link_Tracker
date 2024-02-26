package edu.java.controller.dto;

import java.util.List;

public class ApiErrorResponse {
    private String description;
    private String code;
    private String exceptionName;
    private List<String> stacktrace;
}
