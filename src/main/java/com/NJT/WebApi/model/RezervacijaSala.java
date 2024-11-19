/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.model;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Pera
 */
@Getter
@Setter
@Entity
@Table(name = "rezervacija_sala")
public class RezervacijaSala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rezervacija_id", nullable = false)
    private Rezervacija rezervacija;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    public RezervacijaSala() {

    }

    public RezervacijaSala(Rezervacija rezervacija, Sala sala) {
        this.rezervacija = rezervacija;
        this.sala = sala;
    }
}