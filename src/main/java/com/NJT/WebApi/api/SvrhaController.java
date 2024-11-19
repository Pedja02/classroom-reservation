/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.api;

import com.NJT.WebApi.model.svrha.Svrha;
import com.NJT.WebApi.service.interfaces.ISvrhaService;
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
@RequestMapping("api/v1/svrha")
public class SvrhaController {
    ISvrhaService svrhaService;

    @Autowired
    public SvrhaController(ISvrhaService svrhaService) {
        this.svrhaService = svrhaService;
    }
    
    @GetMapping
    public ResponseEntity<List<Svrha>> getAll(){
        List<Svrha> lista = svrhaService.getAll();
        
        return ResponseEntity.ok(lista);
    }
}
