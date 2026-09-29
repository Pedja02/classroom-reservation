package com.NJT.WebApi.service;

import com.NJT.WebApi.model.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JWTServiceTest {

    private static final String KEY = "unit-test-signing-key";
    private static final String ISSUER = "unit-test-issuer";
    private JWTService service;

    @BeforeEach
    void setUp() {
        service = new JWTService();
        ReflectionTestUtils.setField(service, "algorithmKey", KEY);
        ReflectionTestUtils.setField(service, "issuer", ISSUER);
        ReflectionTestUtils.setField(service, "expireInSeconds", 3600);
        service.init();
    }

    @Test
    void acceptsCreatedToken() {
        User user = new User();
        user.setUsername("pera");

        assertEquals("pera", service.getUsernameFromToken(service.createToken(user)));
    }

    @Test
    void rejectsTamperedPayload() {
        User user = new User();
        user.setUsername("pera");
        String[] parts = service.createToken(user).split("[.]");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        parts[1] = Base64.getUrlEncoder().withoutPadding().encodeToString(
                payload.replace("pera", "admin").getBytes(StandardCharsets.UTF_8));
        String tampered = String.join(".", parts);

        assertThrows(JWTVerificationException.class, () -> service.getUsernameFromToken(tampered));
    }

    @Test
    void rejectsWrongSigningKey() {
        String token = token("wrong-key", ISSUER, Instant.now().plusSeconds(3600));

        assertThrows(JWTVerificationException.class, () -> service.getUsernameFromToken(token));
    }

    @Test
    void rejectsExpiredToken() {
        String token = token(KEY, ISSUER, Instant.now().minusSeconds(60));

        assertThrows(JWTVerificationException.class, () -> service.getUsernameFromToken(token));
    }

    @Test
    void rejectsWrongIssuer() {
        String token = token(KEY, "wrong-issuer", Instant.now().plusSeconds(3600));

        assertThrows(JWTVerificationException.class, () -> service.getUsernameFromToken(token));
    }

    @Test
    void rejectsMissingExpiration() {
        String token = JWT.create().withClaim("username", "pera")
                .withIssuer(ISSUER).sign(Algorithm.HMAC256(KEY));

        assertThrows(JWTVerificationException.class, () -> service.getUsernameFromToken(token));
    }

    @Test
    void stillCreatesVerificationToken() {
        User user = new User();
        user.setEmail("pera@example.com");

        assertEquals(user.getEmail(), JWT.require(Algorithm.HMAC256(KEY))
                .withIssuer(ISSUER).withClaimPresence("exp").build()
                .verify(service.generateVerificationToken(user)).getClaim("email").asString());
    }

    @Test
    void verificationTokenCannotBeUsedAsAuthenticationToken() {
        User user = new User();
        user.setEmail("pera@example.com");
        String token = service.generateVerificationToken(user);

        assertThrows(JWTVerificationException.class, () -> service.getUsernameFromToken(token));
    }

    @Test
    void authenticationTokenWithoutUsernameIsRejected() {
        String token = JWT.create().withIssuer(ISSUER)
                .withExpiresAt(Instant.now().plusSeconds(3600)).sign(Algorithm.HMAC256(KEY));

        assertThrows(JWTVerificationException.class, () -> service.getUsernameFromToken(token));
    }

    @Test
    void authenticationTokenWithBlankUsernameIsRejected() {
        for (String username : new String[] {"", "   "}) {
            String token = JWT.create().withClaim("username", username).withIssuer(ISSUER)
                    .withExpiresAt(Instant.now().plusSeconds(3600)).sign(Algorithm.HMAC256(KEY));

            assertThrows(JWTVerificationException.class, () -> service.getUsernameFromToken(token));
        }
    }

    private String token(String key, String issuer, Instant expiration) {
        return JWT.create().withClaim("username", "pera")
                .withIssuer(issuer).withExpiresAt(expiration).sign(Algorithm.HMAC256(key));
    }
}
