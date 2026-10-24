package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Estados;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadosRepositorio
        extends JpaRepository<Estados, Long> {

    Optional<Estados> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
