package edu.java.service.update;

import edu.java.domain.dto.jdbc.Link;
import java.net.URI;

public interface LinkUpdater {
    void update(Link link);

    boolean support(URI link);
}
