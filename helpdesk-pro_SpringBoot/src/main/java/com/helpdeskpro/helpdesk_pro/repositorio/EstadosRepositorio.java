package com.helpdeskpro.helpdesk_pro.repositorio;

import com.helpdeskpro.helpdesk_pro.entidades.Estados;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadosRepositorio
        extends JpaRepository<Estados, Long> {

    Optional<Estados> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
