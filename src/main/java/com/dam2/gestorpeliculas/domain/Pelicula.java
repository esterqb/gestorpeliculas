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

    private String titulo;
    private int duracion;
    private LocalDate fechaEstreno;
    private String sinopsis;
    private int valoracion;

    @OneToOne
    private FichaTecnica fichaTecnica;

    @ManyToOne
    private Director director;

    @ManyToMany
    @JsonIgnore
    private List<Actor> actores = new ArrayList<>();

    public void addActor(Actor actor){
        if(!actores.contains(actor)){
            actores.add(actor);
            actor.getPeliculas().add(this);
        }
    }
}
