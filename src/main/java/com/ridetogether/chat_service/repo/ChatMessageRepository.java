package com.ridetogether.chat_service.repo;

import com.ridetogether.chat_service.data.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatroomIdOrderBySentDateAsc(Long chatroomId);

    @Query("SELECT DISTINCT c.chatroomId FROM ChatMessage c")
    List<Long> findDistinctChatroomIds();

}

