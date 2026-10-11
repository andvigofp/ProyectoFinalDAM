
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdjuntoRequestDTO {

    private String nombreArchivo;
    private String urlArchivo;
    private String tipoArchivo;
    private Long tamano;
    private Long incidenciaId;
}
