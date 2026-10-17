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
@ToString(exclude = {
        "usuarios", "categorias",
        "prioridades", "estados"
})
@EqualsAndHashCode(exclude = {
        "usuarios", "categorias",
        "prioridades", "estados"
})
@Table(name = "incidencias")
public class Incidencias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, length = 50)
    private String titulo;

    @NonNull
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuarios usuarios;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categorias categorias;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prioridad_id", nullable = false)
    private Prioridades prioridades;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estados estados;

    @Column(columnDefinition = "TEXT")
    private String solucion;

    @OneToMany(mappedBy = "incidencias", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comentarios> comentariosList = new ArrayList<>();

    @OneToMany(mappedBy = "incidencias", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Historiales> historialesList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Usuarios tecnico;
}
