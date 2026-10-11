package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Historiales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialesRepositorio
        extends JpaRepository<Historiales, Long> {

    List<Historiales> findByIncidencia_Id(Long incidenciaId);

    List<Historiales> findByUsuario_Id(Long usuarioId);

    boolean existsByIncidencia_Id(Long incidenciaId);
}
