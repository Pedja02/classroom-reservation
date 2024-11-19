/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.api;

import com.NJT.WebApi.model.Katedra;
import com.NJT.WebApi.service.interfaces.IKatedraService;
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
@RequestMapping("api/v1/katedra")
public class KatedraController {

    IKatedraService katedraService;

    @Autowired
    public KatedraController(IKatedraService katedraService) {
        this.katedraService = katedraService;
    }

    @GetMapping()
    public ResponseEntity<List<Katedra>> getAll() {
        List<Katedra> lista = katedraService.getAll();

        return ResponseEntity.ok(lista);
    }

}
