package com.ridetogether.chat_service.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailRequest {
    private String to;
    private String subject;
    private String body;

}