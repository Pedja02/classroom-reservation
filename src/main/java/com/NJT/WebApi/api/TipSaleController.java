/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.api;

import com.NJT.WebApi.model.TipSale;
import com.NJT.WebApi.service.interfaces.ITipSaleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Pera
 */
@RestController
@RequestMapping("api/v1/tipsale")
@PreAuthorize("hasAuthority('ADMIN')")
public class TipSaleController {
    ITipSaleService tipSaleService;

    @Autowired
    public TipSaleController(ITipSaleService tipSaleService) {
        this.tipSaleService = tipSaleService;
    }
    
    @GetMapping
    public ResponseEntity<List<TipSale>> getAll(){
        List<TipSale> lista = tipSaleService.getAll();
        
        return ResponseEntity.ok(lista);
    }
    
}
