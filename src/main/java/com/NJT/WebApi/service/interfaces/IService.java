/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NJT.WebApi.service.interfaces;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Pera
 * @param <T>
 */
public interface IService <T> {
    public List<T> getAll();
    public Optional<T> getById(Object id);
    public boolean save(T entity);
    public boolean update(T entity);
    public boolean delete(Object id);
}
