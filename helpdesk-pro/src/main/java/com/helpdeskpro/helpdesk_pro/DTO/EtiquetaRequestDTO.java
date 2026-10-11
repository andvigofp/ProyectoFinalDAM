
package com.helpdeskpro.helpdesk_pro.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EtiquetaRequestDTO {

    private String nombre;
    private String descripcion;
}
