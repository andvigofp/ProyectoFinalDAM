
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EtiquetaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
}
