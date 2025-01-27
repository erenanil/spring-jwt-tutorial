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

    public String generateToken(UserDetails userDetails){
        // Map<String, String > claimsMap = new HashMap<>();
        // claimsMap.put("role", "ADMIN");

        return Jwts
        .builder()
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*2)) 
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    public <T> T exportToken(String token, Function<Claims, T> claimsFunction){
        Claims claims = Jwts //Claims, JWT'nin Payload kısmını temsil eden bir nesnedir. Yani, JWT'nin sub, exp, iat gibi bilgileri bu nesne üzerinden okunabilir.
        .parserBuilder()//jwt token'i çözülür.
        .setSigningKey(getKey())
        .build() //Jwts.parserBuilder() ile oluşturulan JWT parser yapılandırmasını tamamlar ve kullanıma hazır bir JWT parser nesnesi döndürür.
        .parseClaimsJws(token) //metoduyla, JWT'nin payload kısmı okunur ve Claims nesnesi haline getirilir.
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
