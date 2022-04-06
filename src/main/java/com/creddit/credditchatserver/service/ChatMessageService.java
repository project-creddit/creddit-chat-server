package com.creddit.credditchatserver.service;

import com.creddit.credditchatserver.entity.ChatMessage;
import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.repository.ChatMessageRepository;
import com.creddit.credditchatserver.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Long createChatMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
        return chatMessage.getId();
    }
}
