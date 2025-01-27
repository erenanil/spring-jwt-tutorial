package com.anileren.service;

import com.anileren.dto.DtoUser;
import com.anileren.jwt.AuthRequest;
import com.anileren.jwt.AuthResponse;

public interface IAuthService {
    public DtoUser register(AuthRequest authRequest);
    public AuthResponse authenticate(AuthRequest authRequest);
}
