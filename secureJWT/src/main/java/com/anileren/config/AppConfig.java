package com.anileren.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.anileren.model.User;
import com.anileren.repository.UserRepository;

@Configuration
public class AppConfig {
    
    @Autowired
    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Optional<User> optinal = userRepository.findByUsername(username);
                if(optinal.isPresent()){
                    return optinal.get();
                }
                return null;
            }
            
        };
    }
    
    
    @Bean
    public AuthenticationProvider authenticationProvider(){
        //Gelen username/password bilgisini alır, UserDetailsService ile DB’den çekilen kullanıcıya ait password ile karşılaştırır.
        
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());   
         
        return authenticationProvider;
        
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
