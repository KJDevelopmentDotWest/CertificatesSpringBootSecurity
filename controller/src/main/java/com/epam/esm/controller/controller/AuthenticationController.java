package com.epam.esm.controller.controller;

import com.epam.esm.controller.dto.AuthenticationRequestDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequestDto authenticationRequestDto) throws ServiceException {

        String token = authService.authenticate(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("username", authenticationRequestDto.getUsername());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
