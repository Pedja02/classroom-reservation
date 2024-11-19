package com.NJT.WebApi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sala")
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "napomena", length = 1024)
    private String napomena;

    @Column(name = "broj_racunara", nullable = false)
    private Integer brojRacunara;

    @Column(name = "broj_mesta", nullable = false)
    private Integer brojMesta;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tip_sale_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipSale tipSale;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "status_sale_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private StatusSale statusSale;

    @Transient
    @JsonIgnore
    private Long tipSaleId;

    @Transient
    @JsonIgnore
    private Long statusSaleId;

    @Override
    public String toString() {
        return naziv;
    }
    
}
