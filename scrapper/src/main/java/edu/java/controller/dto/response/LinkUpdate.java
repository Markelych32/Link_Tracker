package edu.java.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdate {
    @JsonProperty("id")
    @NotNull
    private Long id;
    @JsonProperty("url")
    @NotBlank
    private String url;
    @JsonProperty("description")
    private String description;
    @NotEmpty
    @JsonProperty("tgChatIds")
    private List<Long> tgChatIds;
}
