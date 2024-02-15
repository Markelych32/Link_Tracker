package edu.java.bot.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class User {
    private String name;
    private UserState state;
    private long chatId;
    private List<Link> links = new ArrayList<>();
}
