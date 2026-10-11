package com.helpdeskpro.helpdesk_pro.Entidades;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"incidencias", "etiquetas"})
@EqualsAndHashCode(exclude = {"incidencias", "etiquetas"})
@Table(name = "incidencias_etiquetas")
public class IncidenciasEtiquetas {
    @EmbeddedId
    private IncidenciasEtiquetasPK id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("incidenciasId")
    @JoinColumn(name = "incidencia_id", nullable = false)
    private Incidencias incidencias;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("etiquetaId")
    @JoinColumn(name = "etiqueta_id", nullable = false)
    private Etiquetas etiquetas;

}
