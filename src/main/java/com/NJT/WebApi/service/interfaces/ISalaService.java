/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NJT.WebApi.service.interfaces;

import com.NJT.WebApi.model.Sala;
import java.util.List;

/**
 *
 * @author Pera
 */
public interface ISalaService extends IService<Sala>{
    public List<Sala> getAllActive();
    public List<Sala> getAllByName(String naziv);
}
