package com.helpdeskpro.helpdesk_pro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroRequestDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String password;
    private String telefono;
    private Long departamentoId;
}
