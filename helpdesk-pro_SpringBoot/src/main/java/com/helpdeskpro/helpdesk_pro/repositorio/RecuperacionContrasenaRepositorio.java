package com.helpdeskpro.helpdesk_pro.repositorio;

import com.helpdeskpro.helpdesk_pro.entidades.RecuperacionContrasena;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecuperacionContrasenaRepositorio
        extends JpaRepository<RecuperacionContrasena, Long> {

    List<RecuperacionContrasena> findAllByUsuario_IdAndFechaUsoIsNull(Long usuarioId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RecuperacionContrasena>
    findFirstByUsuario_IdAndFechaUsoIsNullAndFechaExpiracionAfterOrderByFechaCreacionDesc(
            Long usuarioId,
            LocalDateTime fechaActual
    );
}
