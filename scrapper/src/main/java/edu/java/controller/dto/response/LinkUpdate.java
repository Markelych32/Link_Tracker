package edu.java.controller.dto.response;

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
    @NotNull
    private Long id;
    @NotBlank
    private String url;
    private String description;
    @NotEmpty
    private List<Long> tgChatIds;
}
