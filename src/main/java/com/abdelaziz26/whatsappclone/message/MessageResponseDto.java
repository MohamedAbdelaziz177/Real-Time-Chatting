package com.abdelaziz26.whatsappclone.message;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MessageResponseDto {
    private Long senderId;
    private String content;
    private Long receiverId;
    private LocalDateTime timestamp;

    public MessageResponseDto(MessageRequestDto messageRequestDto, Long senderId) {
        this.content = messageRequestDto.getContent();
        this.timestamp = LocalDateTime.now();
        this.senderId = senderId;
    }
}
