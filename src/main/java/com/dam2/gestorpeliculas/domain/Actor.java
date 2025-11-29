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

//    public void addPelicula(Pelicula pelicula) {
//        if (!peliculas.contains(pelicula)) {
//            peliculas.add(pelicula);
//            pelicula.getActores().add(this);
//        }
//    }

//    public void addPelicula(Pelicula pelicula) {
//        if (peliculas == null) {
//            peliculas = new ArrayList<>();
//        }
//        if (!peliculas.stream().anyMatch(p -> p.getId() != null && p.getId().equals(pelicula.getId()))) {
//            peliculas.add(pelicula);
//
//            if (pelicula.getActores() == null) {
//                pelicula.setActores(new ArrayList<>());
//            }
//            pelicula.getActores().add(this);
//        }
//    }
//    public void addPelicula(Pelicula pelicula) {
//        peliculas.add(pelicula);
//        pelicula.getActores().add(this);
//    }
    public void addPelicula(Pelicula pelicula) {
        if (!peliculas.contains(pelicula)) {
            peliculas.add(pelicula);
        }
        if (!pelicula.getActores().contains(this)) {
            pelicula.getActores().add(this);
        }
    }
}
