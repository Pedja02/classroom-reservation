/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NJT.WebApi.model.auth;

import lombok.*;

/**
 *
 * @author Pera
 */
@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordBody {
    private Long id;
    private String currentPassword;
    private String newPassword;
}
