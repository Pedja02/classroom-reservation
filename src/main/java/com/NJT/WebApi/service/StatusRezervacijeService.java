/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.service;

import com.NJT.WebApi.model.StatusRezervacije;
import com.NJT.WebApi.repository.StatusRezervacijeRepository;
import com.NJT.WebApi.service.interfaces.IStatusRezervacijeService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pera
 */
@Service
public class StatusRezervacijeService implements IStatusRezervacijeService{
    StatusRezervacijeRepository statusRezervacijeRepository;

    @Autowired
    public StatusRezervacijeService(StatusRezervacijeRepository statusRezervacijeRepository) {
        this.statusRezervacijeRepository = statusRezervacijeRepository;
    }
    
    @Override
    public List<StatusRezervacije> getAll() {
        return (List<StatusRezervacije>) statusRezervacijeRepository.findAll();
    }

    //NOT IMPLEMENTED YET
    @Override
    public Optional<StatusRezervacije> getById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean save(StatusRezervacije entity) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean update(StatusRezervacije entity) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean delete(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
