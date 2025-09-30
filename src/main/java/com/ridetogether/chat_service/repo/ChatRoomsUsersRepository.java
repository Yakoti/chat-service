package com.ridetogether.chat_service.repo;

import com.ridetogether.chat_service.data.ChatRoomUsersId;
import com.ridetogether.chat_service.data.ChatRoomsUsers;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface ChatRoomsUsersRepository extends JpaRepository<ChatRoomsUsers, ChatRoomUsersId>{
    List<ChatRoomsUsers> findByUserId(Long userId);
    List<ChatRoomsUsers> findByChatRoomId(Long chatRoomId);
    long countByChatRoomId(Long chatRoomId);
    void deleteByChatRoomId(Long chatRoomId);
    void deleteByUserId(Long userId);
    List<ChatRoomsUsers> findByChatRoomIdAndJoinedFalse(Long chatRoomId);
    Optional<ChatRoomsUsers> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    @Modifying
    @Transactional
    void deleteByChatRoomIdAndUserId(Long chatroomId, Long userId);



}
