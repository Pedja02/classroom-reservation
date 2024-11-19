/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.service;

import com.NJT.WebApi.model.StatusSale;
import com.NJT.WebApi.repository.StatusSaleRepository;
import com.NJT.WebApi.service.interfaces.IStatusSaleService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pera
 */
@Service
public class StatusSaleService implements IStatusSaleService{
    StatusSaleRepository statusSaleRepository;

    @Autowired
    public StatusSaleService(StatusSaleRepository statusSaleRepository) {
        this.statusSaleRepository = statusSaleRepository;
    }
    
    
    @Override
    public List<StatusSale> getAll() {
        return (List<StatusSale>) statusSaleRepository.findAll();
    }

    //NOT IMPLEMENTED YET
    @Override
    public Optional<StatusSale> getById(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean save(StatusSale entity) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean update(StatusSale entity) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean delete(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
