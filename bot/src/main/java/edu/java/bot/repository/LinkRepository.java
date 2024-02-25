package edu.java.bot.repository;

import edu.java.bot.model.Link;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);
}
