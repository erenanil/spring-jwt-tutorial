package com.anileren.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    //Gelen her isteği kontrol eder ve JWT token doğrulaması yapar.
    //bu class'ın amacı esasen controller'ı korumak.
    //önceden controller service repository database diye giden sistemimizin önündeki bir filtre veya bariyer görevi görmektedir.
    //OncePerRequestFilter'ı miras alarak her isteğin controller'a ulaşmadan önce bu katmana düşmesini sağlamaktayız.
    //bu miras alma işlemi gerçekleştirdikten sonra doFilterInternal'ı override etmeliyiz ve bütün işlemleri burada gerçekleştireceğiz. 
    

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    String header; // ihtiyacımız olan header token ve username değişkenlerini oluşturarak başlıyoruz.
    String token;
    String username;

    // Gelen isteğin header'ını kontrol ediyoruz ve header değişkenimizin içine aktarıyoruz.
    header = request.getHeader("Authorization");

    //Header kontrolü
    System.out.println("Header: " + header);

    if (header == null) {
        filterChain.doFilter(request, response);
        return;
    }

    // Token'ı ayırıyoruz
    token = header.substring(7);

    //Token kontrolü
    System.out.println("Token: " + token);

    try {
        // Token'dan username'i çözmeye çalışıyoruz
        username = jwtService.getUsernameByToken(token);

        //Username kontrolü
        System.out.println("Username: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // eğer username null değilse ve SecurityContext içerisinde halihazırda bir Authentication nesnesi yoksa:
            // bu, kullanıcının isteğiyle bir kimlik doğrulama işlemi henüz yapılmadığı anlamına gelir.
        
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // verilen username'e göre kullanıcı bilgileri yüklenir.
            // "UserDetailsService" aracılığıyla username, veritabanında sorgulanır.
            // eğer kullanıcı bulunursa, kullanıcı bilgileri (UserDetails nesnesi) döndürülür.
        
            if (userDetails != null && jwtService.isTokenExpired(token)) {
                // eğer userDetails null değilse (kullanıcı veritabanında bulunduysa)
                // ve JWT token süresi dolmamışsa (isTokenExpired true dönerse) işlem devam eder.
        
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                // yeni bir "UsernamePasswordAuthenticationToken" nesnesi oluşturulur.
                // bu token, kullanıcı adı ve yetkilendirme bilgileri (authorities) içerir.
                // burada parola yerine "null" verilmiştir çünkü kimlik doğrulama zaten token üzerinden yapılıyor.
        
                authentication.setDetails(userDetails);
                // "Authentication" nesnesine, kullanıcı detayları (UserDetails) eklenir.
                // bu, SecurityContext(güvenlik bağlamı)in daha fazla bilgiye erişmesi için kullanılır.
        
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // SecurityContext güncellenir ve kullanıcı kimlik doğrulaması tamamlanmış olarak işaretlenir.
                // artık bu kullanıcı, uygulama içinde yetkilendirilmiş bir kullanıcı olarak tanınır.
            }
        }
        
    } catch (ExpiredJwtException expiredJwtException) {
        System.out.println("Token süresi dolmuştur: " + expiredJwtException.getMessage());
    } catch (Exception e) {
        System.out.println("Genel bir hata oluştu: " + e.getMessage());
    }

    filterChain.doFilter(request, response);
}

    
}
