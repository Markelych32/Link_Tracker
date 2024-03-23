package edu.java.domain.link;

import edu.java.domain.chat.ChatEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "link")
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String url;
    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;
    @Column(name = "last_check")
    private OffsetDateTime lastCheck;
    @ManyToMany(mappedBy = "links")
    private List<ChatEntity> chats = new ArrayList<>();

    public void addChat(ChatEntity chat) {
        this.getChats().add(chat);
        chat.getLinks().add(this);
    }

    public void deleteChat(ChatEntity chat) {
        chat.getLinks().remove(this);
        chats.remove(chat);
    }
}
