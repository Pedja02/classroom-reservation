package com.NJT.WebApi.service;

import com.NJT.WebApi.model.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expireInSeconds}")
    private int expireInSeconds;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    private static final String USERNAME_KEY = "username";
    private static final String EMAIL_KEY = "email";


    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(algorithmKey);
        verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .withClaimPresence("exp")
                .build();
    }


    public String createToken(User user) {
        return JWT.create()
                .withClaim(USERNAME_KEY, user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+(1000*expireInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String generateVerificationToken(User user) {
        return JWT.create()
                .withClaim(EMAIL_KEY, user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+(1000*expireInSeconds)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getEmailFromVerificationToken(String token) {
        String email = verifier.verify(token).getClaim(EMAIL_KEY).asString();
        if (email == null || email.isBlank()) {
            throw new JWTVerificationException("Missing or empty email claim");
        }
        return email;
    }

    public String getUsernameFromToken(String token) {
        String username = verifier.verify(token).getClaim(USERNAME_KEY).asString();
        if (username == null || username.isBlank()) {
            throw new JWTVerificationException("Missing or empty username claim");
        }
        return username;
    }

}
