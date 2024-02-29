package edu.java.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkResponse {

    public static Long counter = 0L;

    @JsonProperty("id")
    private Long id;
    @JsonProperty("url")
    private String url;
}
