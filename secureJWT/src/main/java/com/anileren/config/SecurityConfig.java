package com.anileren.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.anileren.jwt.AuthEntryPoint;
import com.anileren.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //belirtilen url isteklerine izin vermek, yetkilendirmeye tabi tutmak ve gereken aksiyonları gerçekleştirmek için kullandığımız class.


    public static final String AUTHENTICATE ="/authenticate";

    public static final String REGISTER = "/register";

    public static final String REFRESH_TOKEN= "/refreshToken";

    public static final String[] SWAGGER_PATHS = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-ui.html"
    };

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        //bu sınıf içerisinde url adreslerimizi yöneteceğiz.
        
        httpSecurity.csrf().disable()//CSRF korumasını devre dışı bırakır.
        .authorizeHttpRequests(request -> 
        request.requestMatchers(AUTHENTICATE,REGISTER,REFRESH_TOKEN) //gelen requestler'in urlleri tanımlanan AUTHENTICATE ve REGISTER ile eşleşirse,
        .permitAll()//bunları görmezden gelip controller katmanına geçir.
        .requestMatchers(SWAGGER_PATHS).permitAll()
        .anyRequest() // yukardaki iki url haricindeki her şey,
        .authenticated()) //yukardakilerin haricindeki url'leri filter işlemine sokacak, hangi filter işlemine sokacağını de 43. satırda gösterdik.
        .httpBasic().authenticationEntryPoint(authEntryPoint).and()
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  //Sunucu tarafında bir oturum tutulmasını engeller (her istekte token ile yeniden kimlik doğrulaması yapılır.)
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);//JwtAuthenticationFilter, tüm isteklerden önce çalışır ve isteklerin geçerli bir JWT token içerip içermediğini kontrol eder.
        
        return httpSecurity.build();

    }
}
