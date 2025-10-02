package com.ridetogether.chat_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.ridetogether.chat_service.data.ChatRoomCreateRequest;
import com.ridetogether.chat_service.data.ChatRooms;
import com.ridetogether.chat_service.data.ChatRoomsUsers;
import com.ridetogether.chat_service.repo.ChatMessageRepository;
import com.ridetogether.chat_service.repo.ChatRoomsRepository;
import com.ridetogether.chat_service.repo.ChatRoomsUsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomsRepository chatRoomsRepository;

    @Autowired
    private ChatRoomsUsersRepository chatRoomsUsersRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ChatRooms> findAllByUserId(String userId) {
        Long userIdLong = Long.parseLong(userId);
        List<ChatRoomsUsers> memberships = chatRoomsUsersRepository.findByUserId(userIdLong);
        List<Long> roomIds = memberships.stream()
                .map(ChatRoomsUsers::getChatRoomId)
                .toList();
        return chatRoomsRepository.findAllById(roomIds);
    }

    public ChatRooms createRoom(ChatRoomCreateRequest request) {
        ChatRooms room = new ChatRooms();


        room.setJoinedUsers(request.getCreatorName());

        // Remove creator from pending users
        Map<Long, String> pendingUsersMap = new HashMap<>(request.getPendingUsersIdsUsernames());
        pendingUsersMap.values().removeIf(name -> name.equals(request.getCreatorName()));

        String pendingMembers = String.join(",", pendingUsersMap.values());
        room.setPendingUsers(pendingMembers);

        room.setRouteLink(request.getRouteLink());


        room = chatRoomsRepository.save(room);


        for (Map.Entry<Long, String> entry : pendingUsersMap.entrySet()) {
            Long userId = entry.getKey();
            String name = entry.getValue();
            chatRoomsUsersRepository.save(new ChatRoomsUsers(room.getId(), userId, name, false));
        }


        chatRoomsUsersRepository.save(
                new ChatRoomsUsers(room.getId(), request.getCreatorId(), request.getCreatorName(), true)
        );

        return room;
    }


    public List<ChatRoomsUsers> getPendingUsers(Long chatRoomId) {
        // Fetch users where joined = false
        return chatRoomsUsersRepository.findByChatRoomIdAndJoinedFalse(chatRoomId);
    }

    public List<ChatRoomsUsers> getJoinedUsers(Long chatRoomId) {
        // Fetch users where joined = true
        List<ChatRoomsUsers> allUsers = chatRoomsUsersRepository.findByChatRoomId(chatRoomId);
        return allUsers.stream()
                .filter(ChatRoomsUsers::isJoined)
                .toList();
    }

    @Transactional
    @Scheduled(cron = "0 0 15 * * *")
    public void cleanupEmptyRooms() {
        List<ChatRooms> rooms = chatRoomsRepository.findAll();

        for (ChatRooms room : rooms) {
            long userCount = chatRoomsUsersRepository.countByChatRoomId(room.getId());
            if (userCount <= 1) {
                chatMessageRepository.deleteByChatroomId(room.getId());
                chatRoomsUsersRepository.deleteByChatRoomId(room.getId());
                chatRoomsRepository.deleteById(room.getId());
            }
        }
    }

    @Transactional
    public void leaveRoom(Long chatRoomId, Long userId) {
        chatRoomsUsersRepository.deleteByChatRoomIdAndUserId(chatRoomId, userId);
        updateChatRoomJoinedPending(chatRoomId);


    }

    void updateChatRoomJoinedPending(Long chatRoomId) {
        ChatRooms room = chatRoomsRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("Chat room not found"));

        List<ChatRoomsUsers> users = chatRoomsUsersRepository.findByChatRoomId(chatRoomId);

        List<String> joinedUsers = users.stream()
                .filter(ChatRoomsUsers::isJoined)
                .map(ChatRoomsUsers::getName)
                .toList();
        List<String> pendingUsers = users.stream()
                .filter(u -> !u.isJoined())
                .map(ChatRoomsUsers::getName)
                .toList();

        room.setJoinedUsers(String.join(",", joinedUsers));
        room.setPendingUsers(String.join(",", pendingUsers));
        chatRoomsRepository.save(room);
    }
}