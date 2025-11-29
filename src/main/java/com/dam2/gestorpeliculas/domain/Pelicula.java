package com.dam2.gestorpeliculas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "peliculas")
@Data  // ✅ Lombok genera getters, setters, toString, equals, hashCode
@AllArgsConstructor      // ✅ genera constructor con todos los campos
@NoArgsConstructor
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 120)
    private String titulo;
    private int duracion;
    private LocalDate fechaEstreno;
    private String sinopsis;
    private int valoracion;

    @ManyToOne
    private Director director;

    @ManyToMany
    @JsonIgnore
    private List<Actor> actores = new ArrayList<>();

    public void addActor(Actor actor) {
        if (!actores.contains(actor)) {
            actores.add(actor);
        }
        if (!actor.getPeliculas().contains(this)) {
            actor.getPeliculas().add(this);
        }
    }



    @ManyToMany
    @JsonIgnore
    private List<Plataforma> plataformas = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    private List<Categoria> categorias = new ArrayList<>();

    @ManyToMany
    @JsonIgnore
    private List<Idioma> idiomas = new ArrayList<>();

}
