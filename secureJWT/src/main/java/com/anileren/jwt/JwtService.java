package com.anileren.jwt;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private static final String SECRET_KEY= "hJ4FHOsF+8zF+x7BK3OFPndGrTA14We3vCqHZSs0Nsk=";

    public String generateKey(UserDetails userDetails){
        return Jwts
        .builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*2))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
    }


    public <T> T exportToken(String token, Function<Claims, T> claimsFunction){
        Claims claims = Jwts
        .parserBuilder()
        .setSigningKey(getKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
        
        return claimsFunction.apply(claims);
    }

    public String getUsernameByToken(String token){
       return exportToken(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = exportToken(token, Claims::getExpiration);
        if(new Date().before(expirationDate)){
            return true;
        }
        return false;
    } 

    public Key getKey(){
        byte[] decode = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decode);
    }
 }
