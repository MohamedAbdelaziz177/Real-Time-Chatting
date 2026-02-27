package com.abdelaziz26.whatsappclone.message;

import com.abdelaziz26.whatsappclone.security.SecurityContextUtil;
import com.abdelaziz26.whatsappclone.user.User;
import com.abdelaziz26.whatsappclone.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MessageResponseDto sendMessage(MessageRequestDto messageRequestDto, Principal princial) {

        /*  مبتشتغلش مع الويب سوكيت  */
        //User currentUser = securityContextUtil.getCurrentUser().orElseThrow(() -> new AccessDeniedException("User is not logged in"));

        String username = princial.getName();
        User currentUser = userRepository.findByEmail(username).orElseThrow(() -> new AccessDeniedException("User is not logged in"));
        User recipient = userRepository.findByEmail(messageRequestDto.getRecipientEmail()).orElseThrow(() -> new RuntimeException("User is not found"));

        Message message = new Message();
        message.setSender(currentUser);
        message.setRecipient(recipient);
        message.setContent(messageRequestDto.getContent());
        message.setSentAt(LocalDateTime.now());
        messageRepository.save(message);


        MessageResponseDto msgRes = new MessageResponseDto(messageRequestDto, currentUser.getEmail());
        simpMessagingTemplate.convertAndSendToUser(recipient.getUsername(),
                "/queue/messages",
                msgRes
                );

        return msgRes;
    }

}
