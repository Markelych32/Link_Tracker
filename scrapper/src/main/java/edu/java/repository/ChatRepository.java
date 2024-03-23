package edu.java.repository;

import edu.java.domain.chat.ChatEntity;
import edu.java.domain.dto.jdbc.Link;
import edu.java.domain.link.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
}
