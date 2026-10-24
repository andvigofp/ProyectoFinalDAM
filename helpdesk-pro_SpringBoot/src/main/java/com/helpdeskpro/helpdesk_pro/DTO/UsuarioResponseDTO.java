package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;

    private String nombre;

    private String apellidos;

    private String email;

    private String telefono;

    private Boolean activo;

    private Long rolId;

    private String rolNombre;

    private Long departamentoId;

    private String departamentoNombre;
}