package com.helpdeskpro.helpdesk_pro.Seguridad;

import com.helpdeskpro.helpdesk_pro.Entidades.Incidencias;
import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Repositorio.UsuariosRepositorio;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

// Helper con el usuario autenticado y las comprobaciones de permisos
// por rol (según lo definido en el anteproyecto)
@Component
public class SeguridadServicio {

    private final UsuariosRepositorio usuariosRepositorio;

    public SeguridadServicio(UsuariosRepositorio usuariosRepositorio) {
        this.usuariosRepositorio = usuariosRepositorio;
    }

    // Usuario que realiza la petición actual
    public Usuarios obtenerUsuarioActual() {
        Authentication autenticacion =
                SecurityContextHolder.getContext().getAuthentication();

        if (autenticacion == null
                || !autenticacion.isAuthenticated()
                || !(autenticacion.getPrincipal()
                        instanceof DetallesUsuario detalles)) {
            throw new AccessDeniedException(
                    "No hay ningún usuario autenticado"
            );
        }

        return usuariosRepositorio.findById(detalles.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el usuario autenticado"
                ));
    }

    public boolean esEmpleado(Usuarios usuario) {
        return "EMPLEADO".equals(JwtServicio.codigoRol(usuario));
    }

    // Un empleado solo puede acceder a sus propias incidencias;
    // técnicos y administradores acceden a todas
    public void verificarAccesoIncidencia(Incidencias incidencia) {
        Usuarios actual = obtenerUsuarioActual();

        if (esEmpleado(actual)
                && !incidencia.getUsuarios().getId().equals(actual.getId())) {
            throw new AccessDeniedException(
                    "No puedes acceder a las incidencias de otro usuario"
            );
        }
    }
}
