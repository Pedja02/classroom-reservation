package com.NJT.WebApi.model.user;

import com.NJT.WebApi.model.VerificationToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
@Inheritance
@DiscriminatorColumn(name="user_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ZaposleniVanNastave.class, name = "ZaposleniVanNastave"),
        @JsonSubTypes.Type(value = ZaposleniUNastavi.class, name = "ZaposleniUNastavi"),
        @JsonSubTypes.Type(value = Student.class, name = "Student")})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = true, unique = true)
    private String username;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "ime_prezime", nullable = false)
    private String imePrezime;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "odobren", nullable = false)
    @JsonIgnore
    private Boolean odobren = false;

    @Column(name = "potvrdjen_mail", nullable = false)
    @JsonIgnore
    private Boolean potvrdjenMail = false;

    @Transient
    @JsonIgnore
    String type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    @Column(nullable = false)
    private String role;

}