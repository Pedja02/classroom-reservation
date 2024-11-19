/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.service;

import com.NJT.WebApi.model.Sala;
import com.NJT.WebApi.model.StatusSale;
import com.NJT.WebApi.model.TipSale;
import com.NJT.WebApi.repository.SalaRepository;
import com.NJT.WebApi.repository.StatusSaleRepository;
import com.NJT.WebApi.repository.TipSaleRepository;
import com.NJT.WebApi.service.interfaces.ISalaService;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pera
 */
@Service
public class SalaService implements ISalaService {

    SalaRepository salaRepository;
    TipSaleRepository tipSaleRepository;
    StatusSaleRepository statusSaleRepository;

    @Autowired
    public SalaService(SalaRepository salaRepository, TipSaleRepository tipSaleRepository, StatusSaleRepository statusSaleRepository) {
        this.salaRepository = salaRepository;
        this.tipSaleRepository = tipSaleRepository;
        this.statusSaleRepository = statusSaleRepository;
    }

    @Override
    public List<Sala> getAll() {
        return (List<Sala>) salaRepository.findAll();
    }

    @Override
    public List<Sala> getAllActive() {
        return (List<Sala>) salaRepository.findAllByStatus("Aktivna");
    }

    @Override
    public List<Sala> getAllByName(String naziv) {
        return (List<Sala>) salaRepository.findAllByNaziv(naziv);
    }

    @Override
    public Optional<Sala> getById(Object id) {
        return salaRepository.findById(Long.valueOf(id.toString()));
    }

    @Override
    @Transactional
    public boolean save(Sala entity) {
        Sala s = null;
        s = salaRepository.save(entity);

        return s != null;
    }

    @Override
    public boolean update(Sala entity) {
        Optional<Sala> s = salaRepository.findById(entity.getId());

        if (s.isPresent()) {
            Sala sala = s.get();
            sala.setNaziv(entity.getNaziv());
            sala.setBrojMesta(entity.getBrojMesta());
            sala.setBrojRacunara(entity.getBrojRacunara());
            sala.setNapomena(entity.getNapomena());

            if (entity.getTipSale().getId() != null) {
                Optional<TipSale> optionalTipSale = tipSaleRepository.findById(entity.getTipSale().getId());
                optionalTipSale.ifPresent(sala::setTipSale);
            }

            if (entity.getStatusSale().getId() != null) {
                Optional<StatusSale> optionalStatusSale = statusSaleRepository.findById(entity.getStatusSale().getId());
                optionalStatusSale.ifPresent(sala::setStatusSale);
            }

            salaRepository.save(sala);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(Object id) {
        //salaRepository.deleteById(Long.valueOf(id.toString()));

        Optional<Sala> s = salaRepository.findById(Long.valueOf(id.toString()));

        if (s.isPresent()) {
            Sala sala = s.get();
            
            sala.setStatusSale(statusSaleRepository.findBystatus("Neaktivna"));

            salaRepository.save(sala);
            return true;
        }

        return false;

    }

}
