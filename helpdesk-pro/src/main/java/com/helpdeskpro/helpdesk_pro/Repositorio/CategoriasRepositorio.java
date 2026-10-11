package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriasRepositorio
        extends JpaRepository<Categorias, Long> {

    Optional<Categorias> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
