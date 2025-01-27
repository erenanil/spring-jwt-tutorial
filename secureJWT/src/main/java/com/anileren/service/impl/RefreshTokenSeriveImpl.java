package com.anileren.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.jwt.AuthResponse;
import com.anileren.jwt.JwtService;
import com.anileren.jwt.RefreshTokenRequest;
import com.anileren.model.RefreshToken;
import com.anileren.repository.RefreshTokenRepository;
import com.anileren.service.IRefreshTokenService;

@Service
public class RefreshTokenSeriveImpl implements IRefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    RefreshTokenGenerateService refreshTokenGenerateService;

    @Autowired
    JwtService jwtService;

    
    public boolean isRefreshTokenExpired(Date expiredDate){
        return new Date().before(expiredDate);
    }
    

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {

        Optional<RefreshToken> optional = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
        
        if(optional.isEmpty()){
            System.out.println("Refresh token geçersizdir." + request.getRefreshToken());
        }

        RefreshToken refreshToken = optional.get();

        if(!isRefreshTokenExpired(refreshToken.getExpireDate())){
            System.out.println("Token süresi geçmiş."+ refreshToken.getRefreshToken());
        }

        String accessToken = jwtService.generateToken(refreshToken.getUser());
        RefreshToken savedRefreshToken = refreshTokenRepository.save(refreshTokenGenerateService.createRefreshToken(refreshToken.getUser()));

        return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
    }
    
}
