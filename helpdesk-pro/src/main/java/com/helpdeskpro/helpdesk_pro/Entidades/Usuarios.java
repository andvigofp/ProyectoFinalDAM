package com.helpdeskpro.helpdesk_pro.Entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Table(name = "usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, length = 100)
    private String nombre;

    @NonNull
    @Column(nullable = false, length = 100)
    private String apellidos;

    @NonNull
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NonNull
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(length = 50)
    private String telefono;

    @Column(nullable = false)
    private Boolean activo = true;


    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Roles roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamentos departamentos;

    @OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comentarios> comentariosList = new ArrayList<>();

    @OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Incidencias> incidenciasList = new ArrayList<>();

    @OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Historiales> historialesList = new ArrayList<>();
}
