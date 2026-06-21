package com.fraudDetection.FraudGuard.controllers;

import com.fraudDetection.FraudGuard.dto.AuthRequestDto;
import com.fraudDetection.FraudGuard.dto.AuthResponseDto;
import com.fraudDetection.FraudGuard.entities.User;
import com.fraudDetection.FraudGuard.services.JwtService;
import com.fraudDetection.FraudGuard.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody AuthRequestDto request) {
        boolean created = userDetailsService.signupUser(request);
        if (!created) {
            return new ResponseEntity<>("User already present", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully. Please login to get token.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userDetailsService.getUserByUsername(request.getUsername());

            String token = jwtService.generateToken(user);

            return  ResponseEntity.ok(new AuthResponseDto(token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



}
