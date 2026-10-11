
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdjuntoResponseDTO {

    private Long id;
    private String nombreArchivo;
    private String urlArchivo;
    private String tipoArchivo;
    private Long tamano;
    private Long incidenciaId;
}
