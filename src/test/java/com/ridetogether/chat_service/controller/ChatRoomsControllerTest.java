package com.ridetogether.chat_service.controller;

import com.ridetogether.chat_service.data.ChatRoomCreateRequest;
import com.ridetogether.chat_service.data.ChatRooms;
import com.ridetogether.chat_service.data.ChatRoomsUsers;
import com.ridetogether.chat_service.data.EmailRequest;
import com.ridetogether.chat_service.dto.UserDto;
import com.ridetogether.chat_service.repo.ChatMessageRepository;
import com.ridetogether.chat_service.repo.ChatRoomsRepository;
import com.ridetogether.chat_service.repo.ChatRoomsUsersRepository;
import com.ridetogether.chat_service.service.ChatRoomManager;
import com.ridetogether.chat_service.proxy.EmailServiceProxy;
import com.ridetogether.chat_service.proxy.UserServiceProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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

    @Autowired
    private ChatRoomManager chatRoomManager;

    @MockitoBean
    private EmailServiceProxy emailServiceProxy;

    @MockitoBean
    private UserServiceProxy userServiceProxy;

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
        List<ChatRooms> rooms = Arrays.asList(Objects.requireNonNull(response.getBody()));

        assertThat(rooms).hasSize(2);
        assertThat(rooms).anyMatch(r -> r.getJoinedUsers().equals("user42"));
        assertThat(rooms).anyMatch(r -> r.getRouteLink().equals("room-2"));
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

        // Verify persistence
        ChatRooms savedRoom = chatRoomsRepository.findById(room.getId()).orElse(null);
        assertThat(savedRoom).isNotNull();
        assertThat(savedRoom.getJoinedUsers()).isEqualTo("creatorName99");
        assertThat(savedRoom.getPendingUsers()).isEqualTo("pendingUser2,pendingUser3");

        // Also verify the linking table
        List<ChatRoomsUsers> users = chatRoomsUsersRepository.findByChatRoomId(savedRoom.getId());
        assertThat(users).hasSize(3); // 1 creator + 2 pending
        assertThat(users).anyMatch(u -> u.getName().equals("creatorName99") && u.isJoined());
        assertThat(users).anyMatch(u -> u.getName().equals("pendingUser2") && !u.isJoined());
        assertThat(users).anyMatch(u -> u.getName().equals("pendingUser3") && !u.isJoined());
    }

    @Test
    void notifyPendingUsers_SendsEmails() {
        // given: a chat room with one joined + one pending user
        ChatRooms notifyRoom = chatRoomsRepository.save(
                new ChatRooms(null, "creatorUser", "pendingUserX", "notify-room")
        );
        chatRoomsUsersRepository.save(new ChatRoomsUsers(notifyRoom.getId(), 100L, "creatorUser", true));
        chatRoomsUsersRepository.save(new ChatRoomsUsers(notifyRoom.getId(), 200L, "pendingUserX", false));

        // mock user service to return a UserDto for pending user
        UserDto mockUser = new UserDto();
        mockUser.setId(200L);
        mockUser.setName("pendingUserX");
        mockUser.setEmail("pending@example.com");

        when(userServiceProxy.getUserById(200L)).thenReturn(mockUser);

        // when: notifyPendingUsers is called
        chatRoomManager.notifyPendingUsers(notifyRoom);

        // then: email should have been sent to the pending user
        verify(emailServiceProxy, times(1)).sendEmail(any(EmailRequest.class));
    }
}