package com.anileren.controller;

import com.anileren.dto.DtoUser;
import com.anileren.jwt.AuthRequest;
import com.anileren.jwt.AuthResponse;
import com.anileren.jwt.RefreshTokenRequest;

public interface IAuthController {
    public DtoUser register(AuthRequest authRequest);
    

    public AuthResponse authenticate(AuthRequest authRequest);


    public AuthResponse refreshToken(RefreshTokenRequest request);

}
