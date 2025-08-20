package com.ridetogether.chat_service.repo;

import com.ridetogether.chat_service.data.ChatRooms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomsRepository extends JpaRepository<ChatRooms, Long> {
    Optional<ChatRooms>findByName(String name);
}
