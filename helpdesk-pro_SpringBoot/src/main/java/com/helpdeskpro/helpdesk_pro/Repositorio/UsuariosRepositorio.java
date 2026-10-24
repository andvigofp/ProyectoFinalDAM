package com.helpdeskpro.helpdesk_pro.Repositorio;

import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuariosRepositorio extends JpaRepository<Usuarios, Long> {

    // 1. Buscar usuario por correo electrónico
    Optional<Usuarios> findByEmail(String email);

    // 2. Buscar usuario por correo ignorando mayúsculas y minúsculas
    Optional<Usuarios> findByEmailIgnoreCase(String email);

    // 3. Comprobar si un correo ya está registrado
    boolean existsByEmail(String email);

    // 4. Comprobar si un correo ya está registrado,
    // ignorando mayúsculas y minúsculas
    boolean existsByEmailIgnoreCase(String email);

    // 5. Buscar un usuario activo por correo electrónico
    Optional<Usuarios> findByEmailAndActivoTrue(String email);

    // 6. Comprobar si existe un usuario activo con ese correo
    boolean existsByEmailAndActivoTrue(String email);

    // 7. Buscar usuarios por nombre y apellidos
    @Query("""
           SELECT u
           FROM Usuarios u
           WHERE LOWER(u.nombre) = LOWER(:nombre)
             AND LOWER(u.apellidos) = LOWER(:apellidos)
           """)
    Optional<Usuarios> findByNombreYApellidos(
            @Param("nombre") String nombre,
            @Param("apellidos") String apellidos
    );
}
