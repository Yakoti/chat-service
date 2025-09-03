package com.ridetogether.chat_service.controller;

import com.ridetogether.chat_service.data.ChatMessage;
import com.ridetogether.chat_service.service.ChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatMessageService chatMessageService;

    // WebSocket endpoint for sending messages in a chatroom
    @MessageMapping("/chatrooms/{chatroomId}/messages")
    @SendTo("/topic/chatrooms/{chatroomId}/messages")
    public ChatMessage sendMsg(@DestinationVariable String chatroomId, ChatMessage message) throws Exception {
        logger.info("Received message: {}", message);
        message.setSentDate(LocalDateTime.now());

        // Save message
        chatMessageService.save(message);

        return message;
    }
}
