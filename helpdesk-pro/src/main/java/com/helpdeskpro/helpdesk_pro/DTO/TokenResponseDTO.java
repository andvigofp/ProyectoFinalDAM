package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseDTO {

    private String token;
    private String tipo;

    private UsuarioResponseDTO usuario;
}
