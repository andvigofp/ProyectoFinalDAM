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
@Table(name = "estados")
public class Estados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @OneToMany(mappedBy = "estados", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Incidencias> incidenciasList = new ArrayList<>();
}