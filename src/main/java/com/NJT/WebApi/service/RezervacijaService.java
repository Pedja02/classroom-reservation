/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.service;

import com.NJT.WebApi.model.Rezervacija;
import com.NJT.WebApi.model.user.User;
import com.NJT.WebApi.model.exception.EmailFailureException;
import com.NJT.WebApi.repository.RezervacijaRepository;
import com.NJT.WebApi.repository.StatusRezervacijeRepository;
import com.NJT.WebApi.service.interfaces.IRezervacijaService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



/**
 *
 * @author Pera
 */
@Service
public class RezervacijaService implements IRezervacijaService {

    private RezervacijaRepository repository;
    private StatusRezervacijeRepository statusRezervacijeRepository;
    private EmailService emailService;

    @Autowired
    public RezervacijaService(RezervacijaRepository repository,
            StatusRezervacijeRepository statusRezervacijeRepository, EmailService emailService) {
        this.repository = repository;
        this.statusRezervacijeRepository = statusRezervacijeRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Rezervacija> getAll() {
        return (List<Rezervacija>) repository.findAll();
    }

    @Override
    public List<Rezervacija> getAllByDate(LocalDateTime date) {
        return (List<Rezervacija>) repository.findByVremeDatum(date);
    }

    @Override
    public List<Rezervacija> getAllByStatusRezervacije(String status) {
        return (List<Rezervacija>) repository.findByStatusRezervacije(status);
    }

    //NOT IMPLEMENTED YET
    @Override
    public Optional<Rezervacija> getById(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean save(Rezervacija rezervacija) {

        Rezervacija r = repository.save(rezervacija);

        return r != null;

    }

    @Override
    @Transactional
    public boolean saveRequest(Rezervacija entity) {
        entity.setId(null);
        entity.setUser(authenticatedUser());
        entity.setStatusRezervacije(statusRezervacijeRepository.findBystatus("Na cekanju"));

        Rezervacija rez = repository.save(entity);

        try {
            emailService.posaljiMailZaRezervaciju("Zahtev za rezervaciju",
                    "\n\n"
                    + "Uspesno ste poslali zahtev za rezervaciju! "
                    + "\n\n-----------------------------------------------------------\n\n "
                    + "Srdacno,\n "
                    + "NjtApp2024", rez.getUser().getEmail());
        } catch (EmailFailureException ex) {
            Logger.getLogger(RezervacijaService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rez != null;
    }

    @Override
    public ResponseEntity acceptRequest(Rezervacija entity) {
        Optional<Rezervacija> rezOpt = repository.findById(entity.getId());
        Map<String, String> response = new HashMap<>();

        if (rezOpt.isPresent()) {
            Rezervacija rez = rezOpt.get();
            rez.setStatusRezervacije(statusRezervacijeRepository.findBystatus("Odobrena"));

            repository.save(rez);

            try {
                emailService.posaljiMailZaRezervaciju("Prihvacen zahtev za rezervaciju",
                        "\n\n"
                        + "Vas zahtev za rezervaciju je prihvacen! "
                        + "\n\n-----------------------------------------------------------\n\n "
                        + "Detalji rezervacije: \n\n"
                        + rez.toString() + "\n\n "
                        + "Srdacno,\n "
                        + "NjtApp2024", rez.getUser().getEmail());
            } catch (EmailFailureException ex) {
                Logger.getLogger(RezervacijaService.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }

            response.put("message", "Uspesno prihvacena rezervacija.");
            return new ResponseEntity(response, HttpStatus.OK);
        }
        response.put("message", "Greska prilikom prihvatanja rezervacije!");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity denyRequest(Rezervacija entity) {
        Optional<Rezervacija> rezOpt = repository.findById(entity.getId());
        Map<String, String> response = new HashMap<>();

        if (rezOpt.isPresent()) {
            Rezervacija rez = rezOpt.get();
            rez.setStatusRezervacije(statusRezervacijeRepository.findBystatus("Odbijena"));
            rez.setRazlogOdjave(entity.getRazlogOdjave());

            repository.save(rez);

            try {
                emailService.posaljiMailZaRezervaciju("Odbijen zahtev za rezervaciju",
                        "\n\n"
                        + "Vas zahtev za rezervaciju je odbijen! "
                        + "\n\n-----------------------------------------------------------\n\n "
                        + "Detalji rezervacije: \n\n"
                        + rez.toString() + "\n\n "
                        + "Srdacno,\n "
                        + "NjtApp2024", rez.getUser().getEmail());
            } catch (EmailFailureException ex) {
                Logger.getLogger(RezervacijaService.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.put("message", "Uspesno odbijena rezervacija.");
            return new ResponseEntity(response, HttpStatus.OK);
        }

        response.put("message", "Greska prilikom odbijanja rezervacije!");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity closeReservation(Rezervacija entity) {
        Optional<Rezervacija> rezOpt = repository.findById(entity.getId());
        Map<String, String> response = new HashMap<>();


        if (rezOpt.isPresent()) {
            Rezervacija rez = rezOpt.get();
            requireOwnerOrAdmin(rez);
            if (!rez.getStatusRezervacije().getStatus().equals("Odbijena")) {
                rez.setStatusRezervacije(statusRezervacijeRepository.findBystatus("Odbijena"));
                rez.setRazlogOdjave(entity.getRazlogOdjave());
            } else {
                response.put("message", "Greska prilikom objavljivanja rezervacije!");
                return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            repository.save(rez);
            try {
                emailService.posaljiMailZaRezervaciju("Otkazivanje rezervacije",
                        "\n\n"
                        + "Vasa rezervacija je otkazana! "
                        + "\n\n-----------------------------------------------------------\n\n "
                        + "Detalji rezervacije: \n\n"
                        + rez.toString() + "\n\n "
                        + "Srdacno,\n "
                        + "NjtApp2024", rez.getUser().getEmail());
            } catch (EmailFailureException ex) {
                Logger.getLogger(RezervacijaService.class.getName()).log(Level.SEVERE, null, ex);
            }
            response.put("message", "Uspesno odjavljena rezervacija.");
            return new ResponseEntity(response, HttpStatus.OK);
        }

        response.put("message", "Greska prilikom odjavljivanja rezervacije!");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public boolean update(Rezervacija entity) {
        Optional<Rezervacija> rezOpt = repository.findById(entity.getId());

        if (rezOpt.isPresent()) {
            Rezervacija rez = rezOpt.get();
            requireOwnerOrAdmin(rez);
            rez.setStatusRezervacije(statusRezervacijeRepository.findBystatus("Na cekanju"));
            rez.setSale(entity.getSale());
            rez.setVremeDatum(entity.getVremeDatum());

            repository.save(rez);

            try {
                emailService.posaljiMailZaRezervaciju("Promena rezervacije",
                        "\n\n"
                        + "Vasa rezervacija je promenjena! "
                        + "\n\n-----------------------------------------------------------\n\n "
                        + "Detalji rezervacije: \n\n"
                        + rez.toString() + "\n\n "
                        + "Srdacno,\n "
                        + "NjtApp2024", rez.getUser().getEmail());
            } catch (EmailFailureException ex) {
                Logger.getLogger(RezervacijaService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }

        return false;
    }

    private User authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof User user) || user.getId() == null) {
            throw new AccessDeniedException("Authenticated user required");
        }
        return user;
    }

    private void requireOwnerOrAdmin(Rezervacija existingReservation) {
        User user = authenticatedUser();
        boolean admin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
        boolean owner = existingReservation.getUser() != null
                && user.getId().equals(existingReservation.getUser().getId());
        if (!owner && !admin) {
            throw new AccessDeniedException("Reservation belongs to another user");
        }
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean delete(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
