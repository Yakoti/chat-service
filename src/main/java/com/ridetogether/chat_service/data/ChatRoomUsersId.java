package com.ridetogether.chat_service.data;

import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomUsersId implements Serializable {
    private Long chatRoomId;
    private Long userId;
}
