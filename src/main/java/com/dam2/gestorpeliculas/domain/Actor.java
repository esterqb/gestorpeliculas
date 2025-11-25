package com.dam2.gestorpeliculas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Relación N:M con película
    @ManyToMany(mappedBy = "actores")
    @JsonIgnore
    private List<Pelicula> peliculas = new ArrayList<>();

    public void addPelicula(Pelicula pelicula) {
        if (!peliculas.contains(pelicula)) {
            peliculas.add(pelicula);
            pelicula.getActores().add(this);
        }
    }
}
