package com.NJT.WebApi.security;

import com.NJT.WebApi.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    JWTRequestFilter jwtRequestFilter;
    AppProperties appProperties;

    @Autowired
    public SecurityConfig(JWTRequestFilter jwtRequestFilter, AppProperties appProperties) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.appProperties = appProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                        "/api/v1/auth/login",
                        "/v1/auth/login"
                ).permitAll()
                .anyRequest().authenticated());
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
