package edu.java.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLinksResponse {
    @JsonProperty("links")
    private List<LinkResponse> links;
    @JsonProperty("size")
    private int size;
}
