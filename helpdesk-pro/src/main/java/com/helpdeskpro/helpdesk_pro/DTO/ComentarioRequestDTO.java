
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComentarioRequestDTO {

    private String mensaje;
    private Long usuarioId;
    private Long incidenciaId;
}
