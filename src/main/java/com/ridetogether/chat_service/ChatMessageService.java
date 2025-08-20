package com.ridetogether.chat_service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage save(ChatMessage message){
        return chatMessageRepository.save(message);
    }

    public List<String> getAllRideIds() {
        return chatMessageRepository.findDistinctRideIds();
    }


    public List<ChatMessage> chatHistory(String rideId) {
        List<ChatMessage> messages = chatMessageRepository.findByRideIdOrderBySentDateAsc(rideId);

        for (ChatMessage message : messages) {
            if (message.getFileName() != null && message.getFileType() != null) {
                // If filePath is not set or empty, compute and set it dynamically
                if (message.getFilePath() == null || message.getFilePath().isEmpty()) {
                    message.setFilePath("/uploads/" + message.getFileName()); // or generate your path as needed
                }
            }
        }

        return messages;
    }


    // New method to store file and set path in message
    public ChatMessage saveMessageWithFile(ChatMessage message, byte[] fileBytes) throws IOException {
        // Store file (example: to local disk)
        String filePath = storeFileOnDisk(fileBytes, message.getFileName());
        message.setFilePath(filePath);

        // Optionally clear content if you want
        // message.setContent(null);

        return chatMessageRepository.save(message);
    }

    private String storeFileOnDisk(byte[] bytes, String filename) throws IOException {
        // Define your storage directory e.g. "uploads/"
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Generate unique filename or keep original
        String uniqueFilename = System.currentTimeMillis() + "_" + filename;
        Path filePath = uploadDir.resolve(uniqueFilename);

        Files.write(filePath, bytes);

        // Return relative path or absolute URI accessible by client
        return "/uploads/" + uniqueFilename;
    }
}
