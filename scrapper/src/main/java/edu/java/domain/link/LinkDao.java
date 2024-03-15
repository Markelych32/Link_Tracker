package edu.java.domain.link;

import edu.java.domain.dto.Link;
import java.util.List;
import java.util.Optional;

public interface LinkDao {
    boolean add(Link link);
    boolean remove(String url);
    boolean update(Link link);
    List<Link> findAll();
    Optional<Link> find(String url);
}
