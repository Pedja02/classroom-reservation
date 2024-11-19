/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.NJT.WebApi.repository;

import com.NJT.WebApi.model.Sala;
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
public interface SalaRepository extends CrudRepository<Sala, Long> {

    @Query("SELECT s FROM Sala s WHERE s.statusSale.status = :status ")
    public List<Sala> findAllByStatus(@Param("status") String status);

    @Query("SELECT s FROM Sala s WHERE s.naziv LIKE CONCAT('%', :naziv, '%')")
    public List<Sala> findAllByNaziv(@Param("naziv") String naziv);
}
