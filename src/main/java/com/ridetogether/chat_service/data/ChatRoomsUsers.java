package com.ridetogether.chat_service.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "chat_rooms_users")
@IdClass(ChatRoomUsersId.class)
public class ChatRoomsUsers
{
    @Id
    @Column(name = "chat_room_id")
    private Long chatRoomId;
    @Id
    @Column(name = "user_id")
    private Long userId;
}
