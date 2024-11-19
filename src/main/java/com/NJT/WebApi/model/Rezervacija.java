package com.NJT.WebApi.model;

import com.NJT.WebApi.model.svrha.Svrha;
import com.NJT.WebApi.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Entity
@Table(name = "rezervacija")
public class Rezervacija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /*
    @Column(name = "svrha", nullable = true)
    private String svrha;
    */

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "svrha_id", nullable = false)
    private Svrha svrha;

    @Column(name = "razlogOdjave", nullable = true)
    private String razlogOdjave;

    @Column(name = "vreme_datum", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime vremeDatum;

    @ManyToMany
    @JoinTable(
            name = "rezervacija_sala",
            joinColumns = @JoinColumn(name = "rezervacija_id"),
            inverseJoinColumns = @JoinColumn(name = "sala_id")
    )
    private List<Sala> sale;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_rezervacije_id", nullable = false)
    private StatusRezervacije statusRezervacije;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Rezervacija() {

    }

    @Override
    public String toString() {
        return "Rezervacija datuma: " + vremeDatum.toString()+ " za sale: "+ sale.toString();
    }
    
    
}
