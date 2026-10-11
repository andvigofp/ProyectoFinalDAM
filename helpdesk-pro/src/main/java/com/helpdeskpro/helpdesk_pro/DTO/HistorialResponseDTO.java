
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialResponseDTO {

    private Long id;
    private String accion;
    private LocalDateTime fecha;

    private Long usuarioId;
    private String usuarioNombre;

    private Long incidenciaId;
}
