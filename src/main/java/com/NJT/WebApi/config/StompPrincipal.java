/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.config;

import java.security.Principal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Pera
 */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class StompPrincipal implements Principal {

        private String name;

        @Override
        public String getName() {
            return name;
        }
    }
