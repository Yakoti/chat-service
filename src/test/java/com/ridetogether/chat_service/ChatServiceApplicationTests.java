package com.ridetogether.chat_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
        System.out.println("App started on port: " + port);
    }

}
