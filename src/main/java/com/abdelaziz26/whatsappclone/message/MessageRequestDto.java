package com.abdelaziz26.whatsappclone.message;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MessageRequestDto {

    private String content;
    private Long receiverId;
}
