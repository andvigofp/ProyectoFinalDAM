
package com.helpdeskpro.helpdesk_pro.repositorio;

import com.helpdeskpro.helpdesk_pro.entidades.Incidencias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncidenciasRepositorio
        extends JpaRepository<Incidencias, Long> {

    // Incidencias creadas por un usuario
    List<Incidencias> findByUsuarios_Id(Long usuarioId);

    @Query("""
            SELECT i
            FROM Incidencias i
            WHERE i.usuarios.id = :usuarioId
              AND (:busqueda = '' OR LOWER(i.titulo)
                   LIKE LOWER(CONCAT('%', :busqueda, '%')) ESCAPE '!')
            ORDER BY LOWER(i.titulo), i.id
            """)
    List<Incidencias> buscarPorUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("busqueda") String busqueda
    );

    // Incidencias asignadas a un técnico
    List<Incidencias> findByTecnico_Id(Long tecnicoId);

    @Query("""
            SELECT i
            FROM Incidencias i
            WHERE i.tecnico.id = :tecnicoId
              AND (:busqueda = '' OR LOWER(i.titulo)
                   LIKE LOWER(CONCAT('%', :busqueda, '%')) ESCAPE '!')
            ORDER BY LOWER(i.titulo), i.id
            """)
    List<Incidencias> buscarPorTecnico(
            @Param("tecnicoId") Long tecnicoId,
            @Param("busqueda") String busqueda
    );

    @Query("""
            SELECT i
            FROM Incidencias i
            WHERE :busqueda = '' OR LOWER(i.titulo)
                  LIKE LOWER(CONCAT('%', :busqueda, '%')) ESCAPE '!'
            ORDER BY LOWER(i.titulo), i.id
            """)
    List<Incidencias> buscarTodas(@Param("busqueda") String busqueda);

    // Incidencias filtradas por estado
    List<Incidencias> findByEstados_Id(Long estadoId);

    // Incidencias filtradas por prioridad
    List<Incidencias> findByPrioridades_Id(Long prioridadId);

    // Incidencias filtradas por categoría
    List<Incidencias> findByCategorias_Id(Long categoriaId);

    // Incidencias de un usuario con un estado concreto
    List<Incidencias> findByUsuarios_IdAndEstados_Id(
            Long usuarioId,
            Long estadoId
    );

    // Comprobar si un usuario tiene incidencias
    boolean existsByUsuarios_Id(Long usuarioId);

    // Comprobar si un técnico tiene incidencias asignadas
    boolean existsByTecnico_Id(Long tecnicoId);
}
