package com.ridetogether.chat_service.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    @Column(name="name")
    private String name;

    @Column(name="status")
    private boolean joined;

    public ChatRoomsUsers(Long chatRoomId, Long userId,String name,boolean joined) {
        this.chatRoomId = chatRoomId;
        this.name= name;
        this.userId = userId;
        this.joined = joined;
    }
}
