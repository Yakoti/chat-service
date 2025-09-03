package com.ridetogether.chat_service.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomCreateRequest {
    private Map<Long,String> pendingUsersIdsUsernames; // userId, username
    private Long creatorId;
    private String creatorName;
    private String routeLink;
}
