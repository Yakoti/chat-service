package com.ridetogether.chat_service;

import com.ridetogether.chat_service.ChatMessage;
import com.ridetogether.ridetogether.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat-history")
public class ChatHistoryController {

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/messages/{rideId}")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable String rideId) {
        List<ChatMessage> messages = chatMessageService.chatHistory(rideId);
        return ResponseEntity.ok(messages);
    }

//    @GetMapping("/rides")
//    public ResponseEntity<List<String>> getAllRideIds() {
//        List<String> ids = chatMessageService.getAllRideIds(); // implement in your service/repo
//        return ResponseEntity.ok(ids);
//    }
//


}

