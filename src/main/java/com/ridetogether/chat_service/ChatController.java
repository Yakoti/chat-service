package com.ridetogether.chat_service;

import com.ridetogether.ridetogether.model.ChatMessage;
import com.ridetogether.ridetogether.service.ChatMessageService;
import com.ridetogether.ridetogether.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private AESUtil aesUtil;

    @MessageMapping("/chat/{rideId}")
    @SendTo("/topic/ride/{rideId}")
    public ChatMessage sendMsg(@DestinationVariable String rideId, ChatMessage message) throws Exception {
        logger.info(message.toString());
        message.setSentDate(LocalDateTime.now());

        boolean isFile = message.getFileName() != null && message.getFileType() != null;

        if (!isFile) {
            // Text message: Only encrypt if not already encrypted
            if (Boolean.TRUE.equals(message.getEncrypted())) {
                chatMessageService.save(message);
            } else {
                try {
                    String encrypted = aesUtil.encryptText(message.getContent());
                    message.setContent(encrypted);
                    message.setEncrypted(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    message.setContent("Error encrypting");
                    message.setEncrypted(false);
                }
                chatMessageService.save(message);
            }
        } else {
            message.setEncrypted(true);
            chatMessageService.save(message);
        }
        return message;
    }
}