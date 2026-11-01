package com.helpdeskpro.helpdesk_pro.repositorio;

import com.helpdeskpro.helpdesk_pro.entidades.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepositorio extends JpaRepository<Roles, Long> {

    Optional<Roles> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
