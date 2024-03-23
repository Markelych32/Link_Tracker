package edu.java.scrapper;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.domain.chat.ChatEntity;
import edu.java.domain.dto.jdbc.Link;
import edu.java.domain.link.LinkEntity;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class TestData {
    private TestData() {
    }

    public static Long testThChatId() {
        return 1L;
    }

    public static String testLinkUrl() {
        return "google.com";
    }

    public static List<LinkResponse> emptyListResponse() {
        return List.of();
    }

    public static List<LinkResponse> listResponseWithLinks() {
        return List.of(new LinkResponse(testThChatId(), testLinkUrl()));
    }

    public static ListLinksResponse emptyListLinksResponse() {
        return new ListLinksResponse(emptyListResponse(), 0);
    }

    public static ListLinksResponse listLinksResponseWithLinks() {
        return new ListLinksResponse(listResponseWithLinks(), 1);
    }

    public static AddLinkRequest testAddLinkRequest() {
        return new AddLinkRequest(testLinkUrl());
    }

    public static RemoveLinkRequest testRemoveLinkRequest() {
        return new RemoveLinkRequest(testLinkUrl());
    }

    public static Link testLinkDtoFirst() {
        Link link = new Link();
        link.setUrl("https://test1.com");
        link.setLastUpdate(OffsetDateTime.now());
        link.setLastCheck(OffsetDateTime.now());
        return link;
    }

    public static LinkEntity testLinkEntity1() {
        return new LinkEntity(
            1L,
            "https://test1.com",
            null,
            null,
            new ArrayList<>()
        );
    }

    public static LinkEntity testLinkEntity2() {
        return new LinkEntity(
            1L,
            "https://test1.com",
            OffsetDateTime.parse("2024-02-21T10:15:30+01:00"),
            OffsetDateTime.parse("2024-03-23T10:15:30+01:00"),
            new ArrayList<>()
        );
    }

    public static Link testLinkDtoSecond() {
        Link link = new Link();
        link.setUrl("https://test2.com");
        link.setLastUpdate(OffsetDateTime.now());
        link.setLastCheck(OffsetDateTime.now());
        return link;
    }

    public static ChatEntity testChatEntity1() {
        return new ChatEntity(1L, new ArrayList<>());
    }

    public static ChatEntity testChatEntity2() {
        return new ChatEntity(2L, new ArrayList<>());
    }

}
