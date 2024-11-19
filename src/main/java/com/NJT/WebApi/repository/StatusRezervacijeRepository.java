/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.StatusRezervacije;
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
public interface StatusRezervacijeRepository extends CrudRepository<StatusRezervacije, Long> {

    @Query("SELECT s FROM StatusRezervacije s WHERE s.status = :status")
    public StatusRezervacije findBystatus(@Param("status") String status);
}
