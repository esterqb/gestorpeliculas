package com.dam2.gestorpeliculas.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "criticas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Critica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;
    private int nota;
    private LocalDate fecha;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Pelicula pelicula;
}
