package com.helpdeskpro.helpdesk_pro.Entidades;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString(exclude = "incidencias")
@EqualsAndHashCode(exclude = "incidencias")
@Table(name = "adjuntos")
public class Adjuntos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "nombre_archivo", nullable = false, length = 200)
    private String nombreArchivo;

    @NonNull
    @Column(name = "url_archivo", nullable = false, length = 500)
    private String urlArchivo;

    @Column(name = "tipo_archivo", length = 100)
    private String tipoArchivo;

    private Long tamano;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidencia_id", nullable = false)
    private Incidencias incidencias;
}
