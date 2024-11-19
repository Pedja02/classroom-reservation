/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.api;

import com.NJT.WebApi.model.StatusSale;
import com.NJT.WebApi.service.interfaces.IStatusSaleService;
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
@RequestMapping("api/v1/statussale")
@PreAuthorize("hasAuthority('ADMIN')")
public class StatusSaleController {
    IStatusSaleService statusSaleService;

    @Autowired
    public StatusSaleController(IStatusSaleService statusSaleService) {
        this.statusSaleService = statusSaleService;
    }
    
    @GetMapping
    public ResponseEntity<List<StatusSale>> getAll(){
        List<StatusSale> lista = statusSaleService.getAll();
        
        return ResponseEntity.ok(lista);
    }
}
