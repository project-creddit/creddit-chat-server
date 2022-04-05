package com.creddit.credditchatserver.repository;

import com.creddit.credditchatserver.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
