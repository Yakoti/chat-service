package com.ridetogether.chat_service.service;

import com.ridetogether.chat_service.data.ChatRoomsUsers;
import com.ridetogether.chat_service.repo.ChatRoomsUsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomInvitationService {
    @Autowired
    private ChatRoomsUsersRepository chatRoomsUsersRepository;

    @Transactional
    public void acceptInvitation(Long chatRoomId, Long userId) {
        ChatRoomsUsers record = chatRoomsUsersRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        record.setJoined(true);
        chatRoomsUsersRepository.save(record);
    }
    @Transactional
    public void declineInvitation(Long chatRoomId, Long userId) {
        chatRoomsUsersRepository.deleteByChatRoomIdAndUserId(chatRoomId, userId);
    }
}
