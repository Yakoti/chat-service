package com.ridetogether.chat_service.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomCreateRequest {
    private List<Long> membersIds;
    private Long creatorId;
    private String joinedUsers;
    private String pending;
    private String routeLink;
}
