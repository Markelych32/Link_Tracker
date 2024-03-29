package edu.java.service.update;

import edu.java.domain.dto.Link;
import java.net.URI;

public interface LinkUpdater {
    void update(Link link);

    boolean support(URI link);
}
