package edu.java.stackOverflow;

import java.util.List;

public record QuestionResponse(
    List<ItemResponse> items
) {
}
