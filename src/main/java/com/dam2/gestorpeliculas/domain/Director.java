package com.dam2.gestorpeliculas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "directores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Relación 1:N con película
    @OneToMany(mappedBy = "director")
    @JsonIgnore
    private List<Pelicula> peliculas;

}