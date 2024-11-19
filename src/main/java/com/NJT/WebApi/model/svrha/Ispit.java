package com.NJT.WebApi.model.svrha;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("ispit")
public class Ispit extends Svrha{

    @Column(name = "tipspita", nullable = true)
    private String tipspita;

    @Column(name = "predmet", nullable = true)
    private String predmet;

}
