package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Comentarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentariosRepositorio
        extends JpaRepository<Comentarios, Long> {

    List<Comentarios> findByIncidencia_Id(Long incidenciaId);

    List<Comentarios> findByUsuario_Id(Long usuarioId);

    boolean existsByIncidencia_Id(Long incidenciaId);
}
