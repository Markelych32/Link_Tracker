package edu.java.bot.controller.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ApiErrorResponse {
    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private List<String> stacktrace;
}
