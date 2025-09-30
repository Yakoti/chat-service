package com.ridetogether.chat_service.service;

import com.ridetogether.chat_service.data.ChatRooms;
import com.ridetogether.chat_service.data.ChatRoomsUsers;
import com.ridetogether.chat_service.dto.UserDto;
import com.ridetogether.email_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomManager3 {

    private final ChatRoomService chatRoomService;
    private final UserClient userClient;
    private final EmailService emailService;


    @Value("${frontend.chat.url}")
    private String frontendChatUrl;

    /**
     * Notify all pending users of a newly created chat room via email.
     */
    public void notifyPendingUsers(ChatRooms room) {
        // Fetch pending and joined users from the DB
        List<ChatRoomsUsers> pendingUsers = chatRoomService.getPendingUsers(room.getId());
        List<ChatRoomsUsers> joinedUsers = chatRoomService.getJoinedUsers(room.getId());


        List<String> joinedUserNames = joinedUsers.stream()
                .map(ChatRoomsUsers::getName)
                .toList();

        for (ChatRoomsUsers pendingUser : pendingUsers) {

            UserDto userDto = userClient.getUserById(pendingUser.getUserId());
            if (userDto != null) {

                List<String> otherPending = pendingUsers.stream()
                        .filter(u -> !u.getUserId().equals(pendingUser.getUserId()))
                        .map(ChatRoomsUsers::getName)
                        .toList();

                // Build email body
                StringBuilder emailBody = new StringBuilder("Hi " + userDto.getName() + ",\n\n");
                emailBody.append("You have been invited to join a chat room.\n\n");
                emailBody.append("Already joined: ").append(String.join(", ", joinedUserNames)).append("\n");
                if (!otherPending.isEmpty()) {
                    emailBody.append("Other pending invitations: ").append(String.join(", ", otherPending)).append("\n");
                }
                emailBody.append("\nClick here to join the chat: ").append(frontendChatUrl);

                // Send email
                emailService.sendEmail(
                        userDto.getEmail(),
                        "You are invited to a chat room",
                        emailBody.toString()
                );
            }
        }
    }
}
