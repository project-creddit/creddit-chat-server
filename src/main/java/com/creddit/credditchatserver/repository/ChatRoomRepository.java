package com.creddit.credditchatserver.repository;

import com.creddit.credditchatserver.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    ChatRoom findByUuid(String uuid);
}
