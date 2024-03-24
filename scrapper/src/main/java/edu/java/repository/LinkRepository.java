package edu.java.repository;

import edu.java.domain.link.LinkEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
    boolean existsByUrl(String utl);

    Optional<LinkEntity> findByUrl(String url);
}
