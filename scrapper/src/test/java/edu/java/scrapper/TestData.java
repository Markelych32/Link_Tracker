package edu.java.scrapper;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.controller.dto.request.RemoveLinkRequest;
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

}
