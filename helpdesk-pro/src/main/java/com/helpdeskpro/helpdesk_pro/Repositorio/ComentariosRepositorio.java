
package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Comentarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentariosRepositorio
        extends JpaRepository<Comentarios, Long> {

    // Buscar comentarios de una incidencia
    List<Comentarios> findByIncidencias_Id(Long incidenciaId);

    // Buscar comentarios de un usuario
    List<Comentarios> findByUsuarios_Id(Long usuarioId);

    // Comprobar si existen comentarios para una incidencia
    boolean existsByIncidencias_Id(Long incidenciaId);
}
