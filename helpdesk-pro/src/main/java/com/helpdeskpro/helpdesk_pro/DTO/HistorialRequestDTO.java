
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialRequestDTO {

    private String accion;
    private Long usuarioId;
    private Long incidenciaId;
}
