/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.StatusSale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Pera
 */
@Repository
public interface StatusSaleRepository extends CrudRepository<StatusSale, Long>{
    @Query("SELECT s FROM StatusSale s WHERE s.status = :status")
    public StatusSale findBystatus(@Param("status") String status);
}
