package com.ridetogether.chat_service.proxy;

import com.ridetogether.chat_service.data.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("emailservice")
public interface EmailServiceProxy {

    @PostMapping("/email/send")
    void sendEmail(@RequestBody EmailRequest request);
}