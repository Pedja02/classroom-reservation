/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.service;

import com.NJT.WebApi.model.TipSale;
import com.NJT.WebApi.repository.TipSaleRepository;
import com.NJT.WebApi.service.interfaces.ITipSaleService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pera
 */
@Service
public class TipSaleService implements ITipSaleService{
    TipSaleRepository tipSaleRepository;

    @Autowired
    public TipSaleService(TipSaleRepository tipSaleRepository) {
        this.tipSaleRepository = tipSaleRepository;
    }

    
    @Override
    public List<TipSale> getAll() {
        return (List<TipSale>) tipSaleRepository.findAll();
    }

    //NOT IMPLEMENTED YET
    @Override
    public Optional<TipSale> getById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean save(TipSale entity) {
            throw new UnsupportedOperationException("Not supported yet.");
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean update(TipSale entity) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean delete(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
