package edu.java.bot.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdate {
    @NotBlank
    private String url;
    @JsonProperty("description")
    private String description;
    @NotEmpty
    @JsonProperty("tgChatIds")
    private List<Long> tgChatIds;
}
