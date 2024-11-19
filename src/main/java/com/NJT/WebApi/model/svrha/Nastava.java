package com.NJT.WebApi.model.svrha;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("nastava")
public class Nastava extends Svrha{

    @Column(name = "tipNastave", nullable = true)
    private String tipNastave;

    @Column(name = "predmet", nullable = true)
    private String predmet;

}
