package com.ridetogether.chat_service.service;
import java.util.List;
import com.ridetogether.chat_service.data.ChatRoomCreateRequest;
import com.ridetogether.chat_service.data.ChatRooms;
import com.ridetogether.chat_service.data.ChatRoomsUsers;
import com.ridetogether.chat_service.repo.ChatRoomsRepository;
import com.ridetogether.chat_service.repo.ChatRoomsUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomsRepository chatRoomsRepository;

    @Autowired
    private ChatRoomsUsersRepository chatRoomsUsersRepository;

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
        room.setName(request.getName());
        room.setRouteLink(request.getRouteLink());
        room = chatRoomsRepository.save(room);

        for (Long userId : request.getUserIds()) {
            chatRoomsUsersRepository.save(new ChatRoomsUsers(room.getId(), userId));
        }



        return room;
    }
}