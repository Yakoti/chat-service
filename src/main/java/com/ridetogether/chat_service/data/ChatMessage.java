package com.ridetogether.chat_service.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String sender;
    private String senderName;
    private Long chatroomId;

    @Column(nullable = true, columnDefinition = "TEXT")
    @Lob
    private String content;
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentDate;
    private String fileName;
    private String fileType;
    //    private Boolean  encrypted;
    private String filePath;


}
