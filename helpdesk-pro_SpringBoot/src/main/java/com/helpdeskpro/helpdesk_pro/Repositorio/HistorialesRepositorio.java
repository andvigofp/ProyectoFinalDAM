package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Historiales;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialesRepositorio
        extends JpaRepository<Historiales, Long> {

    List<Historiales> findByIncidencias_Id(Long incidenciaId);

    List<Historiales> findByUsuarios_Id(Long usuarioId);

    boolean existsByIncidencias_Id(Long incidenciaId);
}