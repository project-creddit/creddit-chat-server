package com.creddit.credditchatserver.service;

import com.creddit.credditchatserver.dto.ChatMessageDto;
import com.creddit.credditchatserver.entity.ChatMessage;
import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.repository.ChatMessageRepository;
import com.creddit.credditchatserver.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Long createChatMessage(ChatMessageDto chatMessageDto) {
        log.debug("chatMessageService: {}", chatMessageDto.getMessage());
        ChatRoom chatRoom = chatRoomRepository.findByUuid(chatMessageDto.getChatRoomId());
        ChatMessage chatMessage = ChatMessage.createChatMessage(chatMessageDto, chatRoom);
        chatMessageRepository.save(chatMessage);
        return chatMessage.getId();
    }
}
