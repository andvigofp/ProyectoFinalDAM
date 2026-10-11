
package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.Repositorio.UsuariosRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ActualizarPasswordTemporal implements CommandLineRunner {

    private final UsuariosRepositorio usuariosRepositorio;
    private final PasswordEncoder passwordEncoder;

    public ActualizarPasswordTemporal(
            UsuariosRepositorio usuariosRepositorio,
            PasswordEncoder passwordEncoder) {
        this.usuariosRepositorio = usuariosRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Usuarios usuario = usuariosRepositorio
                .findByEmailIgnoreCase("andres@helpdesk.com")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No existe andres@helpdesk.com en MySQL"));

        usuario.setPasswordHash(
                passwordEncoder.encode("Admin123!")
        );

        usuariosRepositorio.save(usuario);

        System.out.println(
                "Contraseña de prueba actualizada correctamente."
        );
    }
}
