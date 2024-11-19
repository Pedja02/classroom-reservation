/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.api;

import com.NJT.WebApi.model.StatusRezervacije;
import com.NJT.WebApi.service.interfaces.IStatusRezervacijeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Pera
 */
@RestController
@RequestMapping("api/v1/statusrezervacije")
public class StatusRezervacijeController {
    IStatusRezervacijeService statusRezervacijeService;

   @Autowired
    public StatusRezervacijeController(IStatusRezervacijeService statusRezervacijeService) {
        this.statusRezervacijeService = statusRezervacijeService;
    }
    
    @GetMapping
    public ResponseEntity<List<StatusRezervacije>> getAll(){
        List<StatusRezervacije> lista = statusRezervacijeService.getAll();
        
        return ResponseEntity.ok(lista);
    }
}
