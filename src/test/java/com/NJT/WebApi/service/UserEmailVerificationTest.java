package com.NJT.WebApi.service;

import com.NJT.WebApi.model.VerificationToken;
import com.NJT.WebApi.model.user.User;
import com.NJT.WebApi.repository.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserEmailVerificationTest {
    private static final String KEY = "email-test-key";
    private static final String ISSUER = "email-test-issuer";
    private UserRepository users;
    private VerificationTokenRepository tokens;
    private JWTService jwt;
    private UserService service;
    private User user;

    @BeforeEach
    void setUp() {
        users = mock(UserRepository.class);
        tokens = mock(VerificationTokenRepository.class);
        jwt = new JWTService();
        ReflectionTestUtils.setField(jwt, "algorithmKey", KEY);
        ReflectionTestUtils.setField(jwt, "issuer", ISSUER);
        ReflectionTestUtils.setField(jwt, "expireInSeconds", 3600);
        jwt.init();
        service = new UserService(mock(StudentRepository.class),
                mock(ZaposleniVanNastaveRepository.class), mock(ZaposleniUNastaviRepository.class),
                users, mock(EncryptionService.class), jwt, mock(EmailService.class), tokens);
        user = new User();
        user.setEmail("pera@example.com");
    }

    @Test
    void validVerificationTokenConfirmsUser() {
        String token = jwt.generateVerificationToken(user);
        store(token);
        assertTrue(service.verifyUser(token));
        assertTrue(user.getPotvrdjenMail());
        verify(users).save(user);
        verify(tokens).deleteByUser(user);
    }

    @Test
    void expiredVerificationTokenCannotConfirmUser() {
        rejected(token(KEY, ISSUER, user.getEmail(), Instant.now().minusSeconds(60)));
    }

    @Test
    void wrongSignatureCannotConfirmUser() {
        rejected(token("wrong-key", ISSUER, user.getEmail(), Instant.now().plusSeconds(3600)));
    }

    @Test
    void wrongIssuerCannotConfirmUser() {
        rejected(token(KEY, "wrong-issuer", user.getEmail(), Instant.now().plusSeconds(3600)));
    }

    @Test
    void missingEmailCannotConfirmUser() {
        rejected(JWT.create().withIssuer(ISSUER).withExpiresAt(Instant.now().plusSeconds(3600))
                .sign(Algorithm.HMAC256(KEY)));
    }

    @Test
    void blankEmailCannotConfirmUser() {
        rejected(token(KEY, ISSUER, "  ", Instant.now().plusSeconds(3600)));
    }

    @Test
    void mismatchedEmailCannotConfirmUser() {
        rejected(token(KEY, ISSUER, "other@example.com", Instant.now().plusSeconds(3600)));
    }

    @Test
    void tokenAbsentFromDatabaseCannotConfirmUser() {
        String token = jwt.generateVerificationToken(user);
        when(tokens.findByToken(token)).thenReturn(Optional.empty());
        assertFalse(service.verifyUser(token));
        verifyNoInteractions(users);
        verify(tokens, never()).deleteByUser(any());
    }

    private void rejected(String token) {
        store(token);
        assertFalse(service.verifyUser(token));
        assertFalse(user.getPotvrdjenMail());
        verifyNoInteractions(users);
        verify(tokens, never()).deleteByUser(any());
    }

    private void store(String token) {
        VerificationToken stored = new VerificationToken();
        stored.setToken(token);
        stored.setUser(user);
        when(tokens.findByToken(token)).thenReturn(Optional.of(stored));
    }

    private String token(String key, String issuer, String email, Instant expiration) {
        return JWT.create().withClaim("email", email).withIssuer(issuer)
                .withExpiresAt(expiration).sign(Algorithm.HMAC256(key));
    }
}
