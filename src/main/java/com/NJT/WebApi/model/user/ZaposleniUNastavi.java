package com.NJT.WebApi.model.user;

import com.NJT.WebApi.model.Katedra;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@DiscriminatorValue("zaposleni_u_nastavi")
public class ZaposleniUNastavi extends User {
    @Column(name = "zvanje", nullable = true)
    private String zvanje;

    @ManyToOne(optional = false)
    @JoinColumn(name = "katedra_id", nullable = false)
    private Katedra katedra;

    @Column(name = "titula", nullable = true)
    private String titula;

}