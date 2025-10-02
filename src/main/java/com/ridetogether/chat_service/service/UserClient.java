package com.ridetogether.chat_service.service;

import com.ridetogether.chat_service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserClient {

    @Value("${user.service.url}") // inject from application.properties
    private String userServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public UserDto getUserById(Long id) {
        String url = userServiceUrl + "/users/" + id; // absolute URL now
        return restTemplate.getForObject(url, UserDto.class);
    }
}
