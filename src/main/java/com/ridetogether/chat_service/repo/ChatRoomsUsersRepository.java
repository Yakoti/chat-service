package com.ridetogether.chat_service.repo;

import com.ridetogether.chat_service.data.ChatRoomUsersId;
import com.ridetogether.chat_service.data.ChatRoomsUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomsUsersRepository extends JpaRepository<ChatRoomsUsers, ChatRoomUsersId>{
    List<ChatRoomsUsers> findByUserId(Long userId);
    List<ChatRoomsUsers> findByChatRoomId(Long chatRoomId);

    void deleteByChatRoomId(Long chatRoomId);
    void deleteByUserId(Long userId);


}
