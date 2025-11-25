package com.dam2.gestorpeliculas.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fichas_tecnicas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaTecnica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Director director;

    private int duracion;
    private String pais;
}
