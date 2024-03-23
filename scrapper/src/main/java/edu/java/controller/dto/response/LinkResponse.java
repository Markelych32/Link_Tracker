package edu.java.controller.dto.response;

import edu.java.domain.dto.jdbc.Link;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkResponse {

    public static Long counter = 0L;

    private Long id;
    private String url;

    public static LinkResponse linkDtoToLinkResponse(Link link) {
        return new LinkResponse(
            link.getId(),
            link.getUrl()
        );
    }
}
