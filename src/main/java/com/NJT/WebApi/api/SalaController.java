/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.api;

import com.NJT.WebApi.model.Sala;
import com.NJT.WebApi.service.interfaces.ISalaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Pera
 */
@RestController
@RequestMapping("api/v1/sala")
public class SalaController {

    ISalaService salaService;

    @Autowired
    public SalaController(ISalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public ResponseEntity<List<Sala>> getAll() {
        List<Sala> lista = salaService.getAll();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Sala>> getAllActive() {
        List<Sala> lista = salaService.getAllActive();

        return ResponseEntity.ok(lista);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<Sala>> getAllByName(@RequestParam("naziv") String naziv) {
        List<Sala> lista = salaService.getAllByName(naziv);

        return ResponseEntity.ok(lista);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity save(@RequestBody Sala sala) {
        Map<String, String> response = new HashMap<>();

        if(salaService.save(sala)){
            response.put("message", "Uspesno sacuvana sala.");
            return new ResponseEntity(response, HttpStatus.OK);
        }else{
            response.put("message", "Greska prilikom cuvanja sale!");
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity update(@RequestBody Sala sala) {
        Map<String, String> response = new HashMap<>();

        if(salaService.update(sala)){
            response.put("message", "Uspesno azurirana sala.");
            return new ResponseEntity(response, HttpStatus.OK);
        }else{
            response.put("message", "Greska prilikom azuriranja sale!");
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    public ResponseEntity delete(@RequestBody Long id) {
        Map<String, String> response = new HashMap<>();

        if(salaService.delete(id)){
            response.put("message", "Sala sa ID-jem "+id+" je postavljena kao neaktivna.");
            return new ResponseEntity(response, HttpStatus.OK);
        }else{
            response.put("message", "Greska prilikom deaktiviranja sale sa ID-jem: "+id);
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
