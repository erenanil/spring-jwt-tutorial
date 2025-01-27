package com.anileren.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.anileren.controller.IAuthController;
import com.anileren.dto.DtoUser;
import com.anileren.jwt.AuthRequest;
import com.anileren.jwt.AuthResponse;
import com.anileren.jwt.RefreshTokenRequest;
import com.anileren.service.IAuthService;
import com.anileren.service.IRefreshTokenService;

import jakarta.validation.Valid;

@RestController
public class AuthControllerImpl implements IAuthController{

    @Autowired
    IAuthService userService;

    @Autowired
    IRefreshTokenService refreshTokenService;

    @Override
    @PostMapping("/register")
    public DtoUser register(@Valid @RequestBody AuthRequest authRequest) {
        return userService.register(authRequest);
    }

    @Override
    @PostMapping("/authenticate")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        return userService.authenticate(authRequest);
    }

    @PostMapping("/refreshToken")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        
        return refreshTokenService.refreshToken(request);
    }
   
    
    
}
