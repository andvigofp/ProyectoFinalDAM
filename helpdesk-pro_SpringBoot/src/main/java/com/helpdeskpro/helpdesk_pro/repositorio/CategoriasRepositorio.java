package com.helpdeskpro.helpdesk_pro.repositorio;

import com.helpdeskpro.helpdesk_pro.entidades.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriasRepositorio
        extends JpaRepository<Categorias, Long> {

    Optional<Categorias> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
