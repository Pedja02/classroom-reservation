/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.service;

import com.NJT.WebApi.model.svrha.Svrha;
import com.NJT.WebApi.repository.SvrhaRepository;
import com.NJT.WebApi.service.interfaces.ISvrhaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pera
 */
@Service
public class SvrhaService implements ISvrhaService{
    SvrhaRepository svrhaRepository;

    @Autowired
    public SvrhaService(SvrhaRepository svrhaRepository) {
        this.svrhaRepository = svrhaRepository;
    }
    
    @Override
    public List<Svrha> getAll() {
        return (List<Svrha>) svrhaRepository.findAll();
    }

    @Override
    public Optional<Svrha> getById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean save(Svrha entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean update(Svrha entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean delete(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
