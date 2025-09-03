package com.ridetogether.chat_service.service;
import java.util.List;
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
        room.setJoinedUsers(request.getJoinedUsers());
        room.setPending(request.getPending());
        room.setRouteLink(request.getRouteLink());
        room = chatRoomsRepository.save(room);

        for (Long userId : request.getMembersIds()) {
            chatRoomsUsersRepository.save(new ChatRoomsUsers(room.getId(), userId, false));

        }
        chatRoomsUsersRepository.save(new ChatRoomsUsers(room.getId(), request.getCreatorId(), true));

        return room;
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
    }
}