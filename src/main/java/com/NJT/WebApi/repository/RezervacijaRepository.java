/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.Rezervacija;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Pera
 */
@Repository
public interface RezervacijaRepository extends CrudRepository<Rezervacija, Long> {

    @Query("SELECT r FROM Rezervacija r WHERE YEAR(r.vremeDatum) = YEAR(:date) AND MONTH(r.vremeDatum) "
            + "= MONTH(:date) AND DAY(r.vremeDatum) = DAY(:date) AND r.statusRezervacije.status != 'Odbijena' "
            + "ORDER BY r.vremeDatum")
    public List<Rezervacija> findByVremeDatum(@Param("date") LocalDateTime date);

    @Query("SELECT r FROM Rezervacija r WHERE r.statusRezervacije.status = :status ORDER BY r.vremeDatum")
    public List<Rezervacija> findByStatusRezervacije(@Param("status") String status);
}
