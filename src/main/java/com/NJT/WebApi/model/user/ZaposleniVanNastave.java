package com.NJT.WebApi.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@DiscriminatorValue("zaposleni_van_nastave")
public class ZaposleniVanNastave extends User  {
    @Column(name = "titula", nullable = true)
    private String titula;
}