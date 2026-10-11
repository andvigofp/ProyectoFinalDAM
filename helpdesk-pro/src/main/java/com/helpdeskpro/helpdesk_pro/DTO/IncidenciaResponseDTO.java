
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidenciaResponseDTO {

    private Long id;
    private String titulo;
    private String descripcion;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    private Long usuarioId;
    private String usuarioNombre;

    private Long tecnicoId;
    private String tecnicoNombre;

    private Long categoriaId;
    private String categoriaNombre;

    private Long prioridadId;
    private String prioridadNombre;

    private Long estadoId;
    private String estadoNombre;
}
