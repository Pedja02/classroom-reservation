/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NJT.WebApi.service.interfaces;

import com.NJT.WebApi.model.Rezervacija;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pera
 */
public interface IRezervacijaService extends IService<Rezervacija>{
    public List<Rezervacija> getAllByDate(LocalDateTime date);
    public List<Rezervacija> getAllByStatusRezervacije(String status);
    public boolean saveRequest(Rezervacija entity);
    public ResponseEntity acceptRequest(Rezervacija entity);
    public ResponseEntity denyRequest(Rezervacija entity);
    public ResponseEntity closeReservation(Rezervacija entity);
}
