package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequestDTO {
    private String nombre;

    private String apellidos;

    private String email;

    private String password;

    private String telefono;

    private Boolean activo;

    private Long rolId;

    private Long departamentoId;

}