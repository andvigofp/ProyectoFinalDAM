package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Etiquetas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EtiquetasRepositorio
        extends JpaRepository<Etiquetas, Long> {

    Optional<Etiquetas> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
