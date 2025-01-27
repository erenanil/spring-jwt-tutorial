package com.anileren.service;

import com.anileren.jwt.AuthResponse;
import com.anileren.jwt.RefreshTokenRequest;

public interface IRefreshTokenService {
    
    public AuthResponse refreshToken(RefreshTokenRequest request);

}
