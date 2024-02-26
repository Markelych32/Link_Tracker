package edu.java.bot.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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
