package com.helpdeskpro.helpdesk_pro.Entidades;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString(exclude = "usuariosList")
@EqualsAndHashCode(exclude = "usuariosList")
@Table(name = "departamentos")
public class Departamentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    private String descripcion;

    @OneToMany(mappedBy = "departamentos", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuarios> usuariosList = new ArrayList<>();
}
