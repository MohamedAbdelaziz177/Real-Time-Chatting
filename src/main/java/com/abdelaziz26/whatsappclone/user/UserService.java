package com.abdelaziz26.whatsappclone.user;

import com.abdelaziz26.whatsappclone.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(@Valid RegisterDto dto) {
        var user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        var accessToken = jwtService.generateToken(user);
        return new AuthResponse(accessToken, LocalDateTime.now().plusMinutes(60));
    }

    public AuthResponse login(LoginDto dto) {
        Authentication auth =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        var accessToken = jwtService.generateToken((User) auth.getPrincipal());
        return new AuthResponse(accessToken, LocalDateTime.now().plusMinutes(60));
    }

    public UserResponse getUser(@Valid @Email String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user.getName(), user.getEmail());
    }
}
