package com.abdelaziz26.whatsappclone.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private LocalDateTime expires;
}
