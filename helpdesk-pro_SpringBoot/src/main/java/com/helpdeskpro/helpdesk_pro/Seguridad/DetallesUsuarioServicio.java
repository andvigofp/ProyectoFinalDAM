package com.helpdeskpro.helpdesk_pro.Seguridad;

import com.helpdeskpro.helpdesk_pro.Repositorio.UsuariosRepositorio;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DetallesUsuarioServicio implements UserDetailsService {

    private final UsuariosRepositorio usuariosRepositorio;

    public DetallesUsuarioServicio(
            UsuariosRepositorio usuariosRepositorio) {

        this.usuariosRepositorio = usuariosRepositorio;
    }

    // Spring Security carga el usuario por su email (el "username").
    // La transacción mantiene la sesión de Hibernate abierta mientras
    // se lee el rol (relación lazy).
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return usuariosRepositorio.findByEmailAndActivoTrue(email)
                .map(DetallesUsuario::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No existe el usuario con email " + email
                ));
    }
}
