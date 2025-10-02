package com.ridetogether.chat_service.proxy;


import com.ridetogether.chat_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service")//localhost:0990123/+ users/1
public interface UserServiceProxy {

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

}

//How to use:

//create variable of type UserServiceProxy proxy
// UserDto user = proxy.getUserById(1);
