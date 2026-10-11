package com.helpdeskpro.helpdesk_pro.Entidades;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class IncidenciasEtiquetasPK implements Serializable {
    private Long incidenciasId;
    private Long etiquetaId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IncidenciasEtiquetasPK that = (IncidenciasEtiquetasPK) o;
        return Objects.equals(incidenciasId, that.incidenciasId) && Objects.equals(etiquetaId, that.etiquetaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(incidenciasId, etiquetaId);
    }
}
