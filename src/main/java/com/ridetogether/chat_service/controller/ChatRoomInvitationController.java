package com.ridetogether.chat_service.controller;

import com.ridetogether.chat_service.service.ChatRoomInvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat/chatrooms/invites")
public class    ChatRoomInvitationController {

    @Autowired
    private ChatRoomInvitationService invitationService;

    // Accept invitation
    @PatchMapping("/{chatRoomId}/users/{userId}/accept")
    public ResponseEntity<Void> accept(@PathVariable Long chatRoomId, @PathVariable Long userId) {
        invitationService.acceptInvitation(chatRoomId, userId);
        return ResponseEntity.ok().build();
    }

    // Decline invitation
    @DeleteMapping("/{chatRoomId}/users/{userId}/decline")
    public ResponseEntity<Void> decline(@PathVariable Long chatRoomId, @PathVariable Long userId) {
        invitationService.declineInvitation(chatRoomId, userId);
        return ResponseEntity.noContent().build();
    }
}
