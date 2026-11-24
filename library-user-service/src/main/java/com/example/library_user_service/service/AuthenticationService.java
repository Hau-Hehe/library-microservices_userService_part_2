package com.example.library_user_service.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.library_user_service.dto.LoginRequestDto;
import com.example.library_user_service.dto.LoginResponseDto;
import com.example.library_user_service.dto.UserDto;
import com.example.library_user_service.entity.User;
import com.example.library_user_service.exception.UserNotFoundException;
import com.example.library_user_service.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        String token = generateToken(user);

        return LoginResponseDto.builder()
                .token(token)
                .userDto(toUserDto(user))
                .build();

    }

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("userId", user.getId())
                .withClaim("role", user.getRole().toString())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtExpiration, ChronoUnit.SECONDS))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();

        return userDto;
    }
}
