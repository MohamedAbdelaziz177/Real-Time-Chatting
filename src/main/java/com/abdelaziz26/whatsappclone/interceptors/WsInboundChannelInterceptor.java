package com.abdelaziz26.whatsappclone.interceptors;

import com.abdelaziz26.whatsappclone.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;


/*
* Some browsers cannot use Request headers seamlessly when upgrading the TCP connection
* The solution --> intercept Each Stomp Frame and extract the Principal like normal Http Request
*
* */

@Component
@RequiredArgsConstructor
@Slf4j
public class WsInboundChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            log.info(">>> STOMP CONNECT authHeader: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (!jwtService.isExpired(token)) {
                    String username = jwtService.extractUserName(token);

                    UsernamePasswordAuthenticationToken user =
                            new UsernamePasswordAuthenticationToken(username, null, List.of());

                    accessor.setUser(user);
                } else {
                    throw new AccessDeniedException("JWT Token is expired");
                }
            } else {
                throw new AccessDeniedException("Missing or invalid Authorization header");
            }
        }

        return message;
    }
}

