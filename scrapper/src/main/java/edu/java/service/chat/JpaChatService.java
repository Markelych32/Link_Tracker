package edu.java.service.chat;

import edu.java.domain.chat.ChatEntity;
import edu.java.domain.dto.jdbc.Link;
import edu.java.domain.link.LinkEntity;
import edu.java.exception.ChatAlreadyExistException;
import edu.java.exception.ChatNotFoundException;
import edu.java.exception.LinkNotFoundByUrlException;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    @Override
    public void registerChat(Long tgChatId) {
        if (chatRepository.existsById(tgChatId)) {
            throw new ChatAlreadyExistException();
        }
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setId(tgChatId);
        chatRepository.save(chatEntity);
    }

    @Override
    public List<Long> findChatsByLink(Link link) {
        Optional<LinkEntity> linkEntity = linkRepository.findById(link.getId());
        if (linkEntity.isEmpty()) {
            throw new LinkNotFoundByUrlException();
        }
        return linkEntity.map(entity -> entity.getChats().stream().map(ChatEntity::getId).toList()).orElseGet(List::of);
    }

    @Override
    public void deleteChat(Long tgChatId) {
        Optional<ChatEntity> chatEntity = chatRepository.findById(tgChatId);
        if (chatEntity.isEmpty()) {
            throw new ChatNotFoundException();
        }
        for (LinkEntity linkEntity : chatEntity.get().getLinks()) {
            if (linkEntity.getChats().size() == 1) {
                linkRepository.delete(linkEntity);
            }
        }
        chatRepository.deleteById(tgChatId);
    }
}
