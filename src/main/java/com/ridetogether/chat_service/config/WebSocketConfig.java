package com.ridetogether.chat_service.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    //clients connect via websocket (app/browser)
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    //configures how msgs are routed between clients
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");// in memory msg broker (/topic/ride/SofiaPlovdiv);(/topic/ride/OfficeX);
        config.setApplicationDestinationPrefixes("/app");//clients send msgs to /app/chat/rideId (id ex: "SofiaPlovdiv","OfficeX" )
    }

    @Override
    public void configureWebSocketTransport(org.springframework.web.socket.config.annotation.WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(100 * 1024*1024); // 50MB
        registration.setSendBufferSizeLimit(100*1024 * 1024); // 50MB
        registration.setSendTimeLimit(20 * 1000); // 20 seconds
    }
}


