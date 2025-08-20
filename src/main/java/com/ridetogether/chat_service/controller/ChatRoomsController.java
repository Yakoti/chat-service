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
    public ResponseEntity<List<String>> getUserChatrooms(@PathVariable String userId) {
        List<String> rooms = chatRoomService.findAllByUserId(userId).stream()
                .map(ChatRooms::getName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    // POST /chat/chatrooms/{userId} → create a new chatroom for a user
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody ChatRoomCreateRequest body) {
        ChatRooms createdRoom = chatRoomService.createRoom(body);
        return ResponseEntity.ok(createdRoom);
    }
}

