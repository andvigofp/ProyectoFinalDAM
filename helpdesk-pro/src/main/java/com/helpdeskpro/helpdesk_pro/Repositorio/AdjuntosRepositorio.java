package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Adjuntos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdjuntosRepositorio
        extends JpaRepository<Adjuntos, Long> {

    List<Adjuntos> findByIncidencia_Id(Long incidenciaId);

    boolean existsByIncidencia_Id(Long incidenciaId);
}
