package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.IncidenciasEtiquetas;
import com.helpdeskpro.helpdesk_pro.Entidades.IncidenciasEtiquetasPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenciasEtiquetasRepositorio
        extends JpaRepository<IncidenciasEtiquetas, IncidenciasEtiquetasPK> {
}
