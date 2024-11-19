package com.NJT.WebApi.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("student")
public class Student extends User{

    @Column(name = "svrha", nullable = true)
    private String svrha;

    public Student() {

    }

    public Student(String svrha, String username, String password, String email, String imePrezime) {
        this.svrha = svrha;
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
        this.setImePrezime(imePrezime);
    }


}