package com.ridetogether.chat_service.service;

import com.ridetogether.chat_service.data.ChatRooms;
import com.ridetogether.chat_service.data.ChatRoomsUsers;
import com.ridetogether.chat_service.data.EmailRequest;
import com.ridetogether.chat_service.dto.UserDto;
import com.ridetogether.chat_service.proxy.EmailServiceProxy;
import com.ridetogether.chat_service.proxy.UserServiceProxy;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomManager {

    private final ChatRoomService chatRoomService;
    private final UserServiceProxy userProxy;
    private final EmailServiceProxy emailProxy;


    @Value("${frontend.chat.url}")
    private String frontendChatUrl;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceProxy.class);

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

            UserDto userDto = userProxy.getUserById(pendingUser.getUserId());
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
                EmailRequest request = new EmailRequest(
                        userDto.getEmail(),
                        "You are invited to a chat room",
                        emailBody.toString()
                );
                logger.info("Created request: {}", request.toString());
                // Send email
                try{
                    emailProxy.sendEmail(request);
                } catch(Exception e) {
                    logger.error("KO stana e...");
                    e.printStackTrace();
                }
            }
        }
    }
}
