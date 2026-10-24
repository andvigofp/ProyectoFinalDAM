package com.helpdeskpro.helpdesk_pro.Seguridad;

import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Adapta la entidad Usuarios al sistema de usuarios de Spring Security
public class DetallesUsuario implements UserDetails {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final boolean activo;
    private final String rol;

    public DetallesUsuario(Usuarios usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.passwordHash = usuario.getPasswordHash();
        this.activo = usuario.getActivo();
        this.rol = JwtServicio.codigoRol(usuario);
    }

    public Long getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Un usuario desactivado no puede iniciar sesión
    @Override
    public boolean isEnabled() {
        return activo;
    }
}
