package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Departamentos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentosRepositorio
        extends JpaRepository<Departamentos, Long> {

    Optional<Departamentos> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
