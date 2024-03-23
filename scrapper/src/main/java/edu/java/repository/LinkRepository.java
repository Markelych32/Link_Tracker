package edu.java.repository;

import edu.java.domain.chat.ChatEntity;
import edu.java.domain.link.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
    boolean existsByUrl(String utl);
    Optional<LinkEntity> findByUrl(String url);
}
