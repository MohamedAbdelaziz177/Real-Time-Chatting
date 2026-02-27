package com.abdelaziz26.whatsappclone.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("NullableProblems")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
