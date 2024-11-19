/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.service;

import com.NJT.WebApi.model.Katedra;
import com.NJT.WebApi.repository.KatedraRepository;
import com.NJT.WebApi.service.interfaces.IKatedraService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pera
 */
@Service
public class KatedraService implements IKatedraService {

    KatedraRepository katedraRepository;

    @Autowired
    public KatedraService(KatedraRepository katedraRepository) {
        this.katedraRepository = katedraRepository;
    }

    @Override
    public List<Katedra> getAll() {
        return (List<Katedra>) katedraRepository.findAll();
    }

    //NOT IMPLEMENTED YET
    @Override
    public Optional<Katedra> getById(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean save(Katedra entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean update(Katedra entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //NOT IMPLEMENTED YET
    @Override
    public boolean delete(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
