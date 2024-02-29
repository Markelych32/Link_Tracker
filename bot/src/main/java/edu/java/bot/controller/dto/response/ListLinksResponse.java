package edu.java.bot.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import edu.java.bot.controller.dto.response.LinkResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLinksResponse {
    @JsonProperty("links")
    private List<LinkResponse> links;
    @JsonProperty("size")
    private int size;
}
