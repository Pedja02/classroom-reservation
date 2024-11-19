package com.NJT.WebApi.model.svrha;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("dogadjaj")
public class Dogadjaj extends Svrha{

    @Column(name = "nazivDogadjaja", nullable = true)
    private String naziv;



}
