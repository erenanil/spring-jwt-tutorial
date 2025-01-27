package com.anileren.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.anileren.dto.DtoUser;
import com.anileren.jwt.AuthRequest;
import com.anileren.jwt.AuthResponse;
import com.anileren.jwt.JwtService;
import com.anileren.model.RefreshToken;
import com.anileren.model.User;
import com.anileren.repository.RefreshTokenRepository;
import com.anileren.repository.UserRepository;
import com.anileren.service.IAuthService;

@Service
public class AuthServiceImpl implements IAuthService{
   
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @Autowired
    JwtService jwtService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    RefreshTokenGenerateService refreshTokenGenerateService;

    @Override
    public DtoUser register(AuthRequest authRequest) {
        DtoUser dto = new DtoUser();
        User user = new User();

        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        
        User savedUser = userRepository.save(user);

        BeanUtils.copyProperties(savedUser, dto);

        return dto;
    }


    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            
            authenticationProvider.authenticate(auth);

            Optional<User> optionalUser = userRepository.findByUsername(authRequest.getUsername());
            String accessToken = jwtService.generateToken(optionalUser.get());

            RefreshToken refreshToken = refreshTokenGenerateService.createRefreshToken(optionalUser.get());
            refreshTokenRepository.save(refreshToken);

            return new AuthResponse(accessToken, refreshToken.getRefreshToken());

        } catch (Exception e) {

            System.out.println("Kullanici adı veya şifre hatali");

        }

        return null;
        
    }



}
