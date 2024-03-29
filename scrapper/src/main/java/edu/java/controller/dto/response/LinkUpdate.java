package edu.java.controller.dto.response;

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
    @NotEmpty
    private String description;
    @NotEmpty
    private List<Long> tgChatIds;
}
