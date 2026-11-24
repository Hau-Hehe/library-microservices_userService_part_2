package com.example.library_user_service.service;

import com.example.library_user_service.dto.RegisterRequestDto;
import com.example.library_user_service.dto.UserDto;
import com.example.library_user_service.entity.User;
import com.example.library_user_service.exception.UserAlreadyExistException;
import com.example.library_user_service.exception.UserNotFoundException;
import com.example.library_user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerMember(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new UserAlreadyExistException("User Already Exist");
        }

        User user = User.builder()
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .active(true)
                .createdAt(LocalDateTime.now())
                .role(User.UserRole.MEMBER)
                .build();

        User savedUser = userRepository.save(user);
        return toUserDto(savedUser);
    }

    public UserDto registerLibrarian(RegisterRequestDto registerRequestDto) {
        if(userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new UserAlreadyExistException("User Already Exist");
        }

        User user = User.builder()
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .active(true)
                .createdAt(LocalDateTime.now())
                .role(User.UserRole.LIBRARIAN)
                .build();

        User savedUser = userRepository.save(user);
        return toUserDto(savedUser);
    }

    public UserDto getUserProfileInformation(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));

        return toUserDto(user);
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
