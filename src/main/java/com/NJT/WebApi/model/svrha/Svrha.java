package com.NJT.WebApi.model.svrha;

import com.NJT.WebApi.model.user.Student;
import com.NJT.WebApi.model.user.ZaposleniUNastavi;
import com.NJT.WebApi.model.user.ZaposleniVanNastave;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "svrha")
@Inheritance
@DiscriminatorColumn(name="svrha_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dogadjaj.class, name = "Dogadjaj"),
        @JsonSubTypes.Type(value = Ispit.class, name = "Ispit"),
        @JsonSubTypes.Type(value = Nastava.class, name = "Nastava")})
public class Svrha implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

}
