package edu.java.bot.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class LinkUpdate {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("description")
    private String description;
    @JsonProperty("tgChatIds")
    private List<Long> tgChatIds;
}
