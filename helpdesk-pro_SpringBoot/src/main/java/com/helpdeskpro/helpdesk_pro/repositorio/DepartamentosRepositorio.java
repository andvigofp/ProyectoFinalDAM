package com.helpdeskpro.helpdesk_pro.repositorio;

import com.helpdeskpro.helpdesk_pro.entidades.Departamentos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentosRepositorio
        extends JpaRepository<Departamentos, Long> {

    Optional<Departamentos> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
