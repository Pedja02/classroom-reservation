package com.NJT.WebApi.security;

import com.NJT.WebApi.model.user.User;
import com.NJT.WebApi.repository.UserRepository;
import com.NJT.WebApi.service.JWTService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private UserRepository userRepository;

    @Autowired
    public JWTRequestFilter(JWTService jwtService,
                            UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            try {
                String username = jwtService.getUsernameFromToken(token);
                Optional<User> opUser = userRepository.findByUsername(username);
                if (opUser.isPresent()) {
                    User localUser = opUser.get();

                    List<GrantedAuthority> roles;
                    roles = Arrays.stream(localUser.getRole().split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(localUser, null, roles);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //test da li mapiranje radi
                    //System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().getName()+"");

                }
            }catch (JWTVerificationException ex){
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
