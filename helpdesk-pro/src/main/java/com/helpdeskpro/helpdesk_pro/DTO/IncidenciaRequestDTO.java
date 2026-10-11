
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidenciaRequestDTO {

    private String titulo;
    private String descripcion;

    private Long usuarioId;
    private Long tecnicoId;
    private Long categoriaId;
    private Long prioridadId;
    private Long estadoId;
}
