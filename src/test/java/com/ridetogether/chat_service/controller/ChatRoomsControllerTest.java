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
import java.util.List;

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
        // 1️⃣ Clear dependent data first
        chatMessagesRepository.deleteAll();
        chatRoomsUsersRepository.deleteAll();
        chatRoomsRepository.deleteAll();

        // 2️⃣ Create chat rooms
        room1 = chatRoomsRepository.save(new ChatRooms(null, "Room 1", "room-1"));
        room2 = chatRoomsRepository.save(new ChatRooms(null, "Room 2", "room-2"));

        // 3️⃣ Link rooms to test user (userId = 42)
        chatRoomsUsersRepository.save(new ChatRoomsUsers(room1.getId(), 42L));
        chatRoomsUsersRepository.save(new ChatRoomsUsers(room2.getId(), 42L));
    }

    @Test
    void getUserChatrooms_ReturnsRooms() {
        // GET all chat rooms (assuming your endpoint returns all rooms)
        ResponseEntity<ChatRooms[]> response = restTemplate.getForEntity("/chat/chatrooms/42", ChatRooms[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ChatRooms> rooms = Arrays.asList(response.getBody());

        assertThat(rooms).hasSize(2);
        assertThat(rooms.get(0).getName()).isEqualTo("Room 1");
        assertThat(rooms.get(1).getRouteLink()).isEqualTo("room-2");
    }

    @Test
    void createRoom_ReturnsCreatedRoom() {
        ChatRoomCreateRequest request = new ChatRoomCreateRequest(
                Arrays.asList(1L, 2L), // user IDs
                "New Room",
                "new-room"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatRoomCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatRooms> response = restTemplate.postForEntity("/chat/chatrooms", entity, ChatRooms.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ChatRooms room = response.getBody();
        assertThat(room).isNotNull();
        assertThat(room.getName()).isEqualTo("New Room");
        assertThat(room.getRouteLink()).isEqualTo("new-room");

        // Optionally check that the room is saved in the database
        ChatRooms savedRoom = chatRoomsRepository.findById(room.getId()).orElse(null);
        assertThat(savedRoom).isNotNull();
        assertThat(savedRoom.getName()).isEqualTo("New Room");
    }
}
