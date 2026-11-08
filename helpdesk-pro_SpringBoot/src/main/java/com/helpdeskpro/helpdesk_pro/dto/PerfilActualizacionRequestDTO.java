package com.helpdeskpro.helpdesk_pro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilActualizacionRequestDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String password;
}
