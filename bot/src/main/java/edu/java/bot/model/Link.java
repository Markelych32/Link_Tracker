package edu.java.bot.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Link {
    private String url;
    private List<User> users = new ArrayList<>();
}
