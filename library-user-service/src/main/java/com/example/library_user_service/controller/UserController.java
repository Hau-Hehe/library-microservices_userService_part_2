package com.example.library_user_service.controller;

import com.example.library_user_service.dto.LoginRequestDto;
import com.example.library_user_service.dto.LoginResponseDto;
import com.example.library_user_service.dto.RegisterRequestDto;
import com.example.library_user_service.dto.UserDto;
import com.example.library_user_service.service.AuthenticationService;
import com.example.library_user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerMember(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        UserDto response = userService.registerMember(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/librarian")
    public ResponseEntity<UserDto> registerLibrarian(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        UserDto response = userService.registerLibrarian(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        LoginResponseDto responseDto = authenticationService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getuserprofile")
    public ResponseEntity<UserDto> getUserProfileInfo(Authentication authentication){
        UserDto response = userService.getUserProfileInformation(authentication.getName());
        return ResponseEntity.ok(response);
    }
}
