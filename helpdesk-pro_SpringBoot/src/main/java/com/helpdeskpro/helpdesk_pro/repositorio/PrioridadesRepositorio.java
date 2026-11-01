package com.helpdeskpro.helpdesk_pro.repositorio;

import com.helpdeskpro.helpdesk_pro.entidades.Prioridades;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrioridadesRepositorio
        extends JpaRepository<Prioridades, Long> {

    Optional<Prioridades> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
