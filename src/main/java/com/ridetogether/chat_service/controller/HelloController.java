package com.ridetogether.chat_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String HelloTest(){
        return "Hello Testing smth";
    }
}
