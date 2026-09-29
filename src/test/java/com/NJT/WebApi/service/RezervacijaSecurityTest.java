package com.NJT.WebApi.service;

import com.NJT.WebApi.api.RezervacijaController;
import com.NJT.WebApi.model.Rezervacija;
import com.NJT.WebApi.model.StatusRezervacije;
import com.NJT.WebApi.model.user.User;
import com.NJT.WebApi.repository.RezervacijaRepository;
import com.NJT.WebApi.repository.StatusRezervacijeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RezervacijaSecurityTest {
    private RezervacijaRepository repository;
    private StatusRezervacijeRepository statuses;
    private EmailService email;
    private RezervacijaService service;
    private Rezervacija existing;
    private Rezervacija request;
    private User owner;

    @BeforeEach
    void setUp() {
        repository = mock(RezervacijaRepository.class);
        statuses = mock(StatusRezervacijeRepository.class);
        email = mock(EmailService.class);
        service = new RezervacijaService(repository, statuses, email);
        owner = user(1L, "USER");
        existing = new Rezervacija();
        existing.setId(10L);
        existing.setUser(owner);
        existing.setSale(List.of());
        existing.setVremeDatum(LocalDateTime.of(2026, 10, 1, 12, 0));
        existing.setStatusRezervacije(new StatusRezervacije(1L, "Odobrena"));
        request = new Rezervacija();
        request.setId(10L);
        request.setUser(user(2L, "ADMIN"));
        request.setSale(List.of());
        request.setVremeDatum(existing.getVremeDatum().plusDays(1));
        request.setRazlogOdjave("reason");
        when(repository.findById(10L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(statuses.findBystatus(anyString())).thenAnswer(invocation ->
                new StatusRezervacije(2L, invocation.getArgument(0)));
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void creationUsesAuthenticatedUser() {
        authenticate(owner);
        assertTrue(service.saveRequest(request));
        assertSame(owner, request.getUser());
        verify(repository).save(request);
    }

    @Test
    void creationRequiresAuthenticatedUser() {
        assertThrows(AccessDeniedException.class, () -> service.saveRequest(request));
        verify(repository, never()).save(any());
        verifyNoInteractions(email, statuses);
    }

    @Test
    void ownerCanUpdateWithoutChangingOwner() {
        authenticate(user(1L, "USER"));
        assertTrue(service.update(request));
        assertSame(owner, existing.getUser());
        assertEquals(request.getVremeDatum(), existing.getVremeDatum());
        verify(repository).save(existing);
    }

    @Test
    void ownerCanCancel() {
        authenticate(owner);
        assertEquals(200, service.closeReservation(request).getStatusCode().value());
        verify(repository).save(existing);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void otherUserGets403WithoutMutationOrEmail(boolean cancel) throws Exception {
        authenticate(user(2L, "USER"));
        var oldTime = existing.getVremeDatum();
        var oldStatus = existing.getStatusRezervacije();
        var mvc = MockMvcBuilders.standaloneSetup(new RezervacijaController(service))
                .addFilters(new ExceptionTranslationFilter(new Http403ForbiddenEntryPoint()))
                .build();
        var json = new com.fasterxml.jackson.databind.ObjectMapper().createObjectNode().put("id", 10);
        var builder = cancel ? post("/api/v1/rezervacija/odjavi") : put("/api/v1/rezervacija");
        mvc.perform(builder.contentType(MediaType.APPLICATION_JSON).content(json.toString()))
                .andExpect(status().isForbidden());
        assertSame(owner, existing.getUser());
        assertSame(oldStatus, existing.getStatusRezervacije());
        assertEquals(oldTime, existing.getVremeDatum());
        assertNull(existing.getRazlogOdjave());
        verify(repository, never()).save(any());
        verifyNoInteractions(email, statuses);
    }

    @Test
    void adminWithMultipleAuthoritiesCanUpdate() {
        authenticate(user(2L, "USER,ADMIN"));
        assertTrue(service.update(request));
        assertSame(owner, existing.getUser());
        verify(repository).save(existing);
    }

    @Test
    void adminWithMultipleAuthoritiesCanCancel() {
        authenticate(user(2L, "USER,ADMIN"));
        assertEquals(200, service.closeReservation(request).getStatusCode().value());
        assertSame(owner, existing.getUser());
        verify(repository).save(existing);
    }

    @Test
    void creationIgnoresIdFromRequestBody() {
        authenticate(owner);
        request.setId(10L);
        when(repository.save(any())).thenAnswer(invocation -> {
            Rezervacija saved = invocation.getArgument(0);
            assertNull(saved.getId());
            return saved;
        });

        assertTrue(service.saveRequest(request));

        verify(repository).save(request);
        assertEquals(10L, existing.getId());
    }

    private User user(Long id, String role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        user.setEmail("user" + id + "@example.com");
        return user;
    }

    private void authenticate(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null,
                        AuthorityUtils.createAuthorityList(user.getRole().split(","))));
    }
}
