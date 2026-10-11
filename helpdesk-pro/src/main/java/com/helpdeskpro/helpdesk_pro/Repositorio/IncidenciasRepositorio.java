package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Incidencias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciasRepositorio
        extends JpaRepository<Incidencias, Long> {

    // Incidencias creadas por un usuario
    List<Incidencias> findByUsuarios_Id(Long usuarioId);

    // Incidencias asignadas a un técnico
    List<Incidencias> findByTecnico_Id(Long tecnicoId);

    // Incidencias filtradas por estado
    List<Incidencias> findByEstado_Id(Long estadoId);

    // Incidencias filtradas por prioridad
    List<Incidencias> findByPrioridad_Id(Long prioridadId);

    // Incidencias filtradas por categoría
    List<Incidencias> findByCategoria_Id(Long categoriaId);

    // Incidencias creadas por un usuario y con un estado concreto
    List<Incidencias> findByUsuarios_IdAndEstado_Id(
            Long usuarioId,
            Long estadoId
    );

    // Comprobar si un usuario tiene incidencias
    boolean existsByUsuarios_Id(Long usuarioId);

    // Comprobar si un técnico tiene incidencias asignadas
    boolean existsByTecnico_Id(Long tecnicoId);
}
