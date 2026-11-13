package com.helpdeskpro.helpdesk_pro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudRecuperacionContrasenaRequestDTO {
    private String email;
}
