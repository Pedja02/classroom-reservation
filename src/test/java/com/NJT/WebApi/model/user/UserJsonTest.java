package com.NJT.WebApi.model.user;

import com.NJT.WebApi.model.auth.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserJsonTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void userJsonOmitsPassword() throws Exception {
        User user = new User();
        user.setUsername("pera");
        user.setPassword("secret");
        var json = mapper.readTree(mapper.writeValueAsString(user));
        assertFalse(json.has("password"));
        assertEquals("pera", json.get("username").asText());
    }

    @Test
    void loginResponseJsonOmitsPassword() throws Exception {
        User user = new User();
        user.setPassword("secret");
        LoginResponse response = new LoginResponse();
        response.setUser(user);
        var json = mapper.readTree(mapper.writeValueAsString(response));
        assertFalse(json.get("user").has("password"));
    }

    @Test
    void passwordCanStillBeDeserialized() throws Exception {
        var json = mapper.createObjectNode().put("type", "Student").put("password", "secret");
        User user = mapper.treeToValue(json, User.class);
        assertEquals("secret", user.getPassword());
    }
}
