package com.abdelaziz26.whatsappclone.Listeners;

import com.abdelaziz26.whatsappclone.user.User;
import com.abdelaziz26.whatsappclone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WsEventListener {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleWsConnectionEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser().getName();

        updateStatus(username,  true);

        simpMessagingTemplate.convertAndSend("/topic/online", username + "is ONLINE");
    }

    @EventListener
    public void handleWsDisconnectEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser().getName();

        updateStatus(username,  false);

        simpMessagingTemplate.convertAndSend("/topic/online", username + "is ONLINE");
    }

    private void updateStatus(String username, boolean status) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setOnline(status);
        userRepository.save(user);
    }
}
