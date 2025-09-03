package com.ridetogether.chat_service.controller;

import com.ridetogether.chat_service.data.ChatRoomCreateRequest;
import com.ridetogether.chat_service.data.ChatRooms;
import com.ridetogether.chat_service.repo.ChatRoomsRepository;
import com.ridetogether.chat_service.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat/chatrooms")
public class ChatRoomsController {

    @Autowired
    private ChatRoomService chatRoomService;

    // GET /chat/chatrooms/{userId} → list all chatrooms user is in
    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatRooms>> getUserChatrooms(@PathVariable String userId) {
        List<ChatRooms> rooms = chatRoomService.findAllByUserId(userId);
        return ResponseEntity.ok(rooms);
    }


    // POST /chat/chatrooms/{userId} → create a new chatroom for a user
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody ChatRoomCreateRequest body) {
        ChatRooms createdRoom = chatRoomService.createRoom(body);
        return ResponseEntity.ok(createdRoom);
    }

    @DeleteMapping("/{chatRoomId}/users/{userId}/leave")
    public ResponseEntity<Void> leaveRoom(@PathVariable long chatRoomId, @PathVariable long userId) {
        chatRoomService.leaveRoom(chatRoomId, userId);
        return ResponseEntity.noContent().build();
    }
}

