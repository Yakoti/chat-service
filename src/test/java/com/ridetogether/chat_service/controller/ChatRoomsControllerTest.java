package com.ridetogether.chat_service.controller;

import com.ridetogether.chat_service.data.ChatRoomCreateRequest;
import com.ridetogether.chat_service.data.ChatRooms;
import com.ridetogether.chat_service.data.ChatRoomsUsers;
import com.ridetogether.chat_service.repo.ChatMessageRepository;
import com.ridetogether.chat_service.repo.ChatRoomsRepository;
import com.ridetogether.chat_service.repo.ChatRoomsUsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatRoomsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ChatMessageRepository chatMessagesRepository;

    @Autowired
    private ChatRoomsRepository chatRoomsRepository;

    @Autowired
    private ChatRoomsUsersRepository chatRoomsUsersRepository;

    private ChatRooms room1;
    private ChatRooms room2;

    @BeforeEach
    void setup() {
        // Clear dependent data first
        chatMessagesRepository.deleteAll();
        chatRoomsUsersRepository.deleteAll();
        chatRoomsRepository.deleteAll();

        // Create chat rooms with joinedUsers and pending strings as usernames
        room1 = chatRoomsRepository.save(
                new ChatRooms(null, "user42", "pendingUser1", "room-1")
        );
        room2 = chatRoomsRepository.save(
                new ChatRooms(null, "user42", "pendingUser2", "room-2")
        );

        // Save ChatRoomsUsers linking user 42 with proper names and joined status
        chatRoomsUsersRepository.save(new ChatRoomsUsers(room1.getId(), 42L, "user42", true));
        chatRoomsUsersRepository.save(new ChatRoomsUsers(room2.getId(), 42L, "user42", true));
    }

    @Test
    void getUserChatrooms_ReturnsRooms() {
        ResponseEntity<ChatRooms[]> response = restTemplate.getForEntity("/chat/chatrooms/42", ChatRooms[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ChatRooms> rooms = Arrays.asList(response.getBody());

        assertThat(rooms.size()).isEqualTo(2);
        assertThat(rooms.stream().anyMatch(r -> r.getJoinedUsers().equals("user42"))).isTrue();
        assertThat(rooms.stream().anyMatch(r -> r.getRouteLink().equals("room-2"))).isTrue();
    }

    @Test
    void createRoom_ReturnsCreatedRoom() {
        Map<Long, String> pendingUsers = new HashMap<>();
        pendingUsers.put(2L, "pendingUser2");
        pendingUsers.put(3L, "pendingUser3");

        ChatRoomCreateRequest request = new ChatRoomCreateRequest();
        request.setPendingUsersIdsUsernames(pendingUsers);
        request.setCreatorId(99L);
        request.setCreatorName("creatorName99");
        request.setPendingUsersIdsUsernames(pendingUsers);
        request.setRouteLink("new-room");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatRoomCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatRooms> response = restTemplate.postForEntity("/chat/chatrooms", entity, ChatRooms.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ChatRooms room = response.getBody();
        assertThat(room).isNotNull();
        assertThat(room.getJoinedUsers()).isEqualTo("creatorName99");
        assertThat(room.getPendingUsers()).isEqualTo("pendingUser2,pendingUser3");
        assertThat(room.getRouteLink()).isEqualTo("new-room");

        ChatRooms savedRoom = chatRoomsRepository.findById(room.getId()).orElse(null);
        assertThat(savedRoom).isNotNull();
        assertThat(savedRoom.getJoinedUsers()).isEqualTo("creatorName99");
    }
}

