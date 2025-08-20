package com.ridetogether.chat_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRideIdOrderBySentDateAsc(String rideId);

    @Query("SELECT DISTINCT c.rideId FROM ChatMessage c")
    List<String> findDistinctRideIds();
}

