package com.dam2.gestorpeliculas.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plataformas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plataforma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; //netflix hbo prime etc
    private String url;

    @ManyToMany(mappedBy = "plataformas")
    private List<Pelicula> peliculas = new ArrayList<>();
}
