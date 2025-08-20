package com.ridetogether.chat_service.controller;

import com.ridetogether.chat_service.data.ChatMessage;
import com.ridetogether.chat_service.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/chatrooms")
public class ChatHistoryController {

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/{chatroomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long chatroomId) {
        List<ChatMessage> messages = chatMessageService.chatHistory(chatroomId);
        return ResponseEntity.ok(messages);
    }
}
