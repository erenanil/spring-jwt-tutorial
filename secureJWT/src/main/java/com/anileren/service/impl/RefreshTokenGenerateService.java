package com.anileren.service.impl;

import java.sql.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.anileren.model.RefreshToken;
import com.anileren.model.User;

@Service
public class RefreshTokenGenerateService {

    public RefreshToken createRefreshToken (User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(new Date(System.currentTimeMillis()+ 1000*60*60*4));
        
        refreshToken.setUser(user);

        return refreshToken;
    }
}

