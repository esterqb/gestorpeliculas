package com.dam2.gestorpeliculas.service;


import com.dam2.gestorpeliculas.DTO.PeliculaDTO;
import com.dam2.gestorpeliculas.domain.Pelicula;
import lombok.*;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.dam2.gestorpeliculas.repository.PeliculaRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


@Service
@Getter
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;

    private final List<Pelicula> peliculas = new ArrayList<>();

    public PeliculaService() {
        peliculas.add(new Pelicula(1L, "Interstellar", 169, LocalDate.of(2014, 11, 7),
                "Exploradores espaciales buscan un nuevo hogar para la humanidad.", 10, null, null, null));
        peliculas.add(new Pelicula(2L, "The Dark Knight", 152, LocalDate.of(2008, 7, 18),
                "Batman enfrenta al Joker en una lucha por el alma de Gotham.", 5, null, null, null));
        peliculas.add(new Pelicula(3L, "Soul", 100, LocalDate.of(2020, 12, 25),
                "Un músico descubre el sentido de la vida más allá de la muerte.", 8, null, null, null));
    }

    public List<PeliculaDTO> listar() {
        return peliculaRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
        //pasar por dto
    }

    private PeliculaDTO toDto(Pelicula pelicula) {
        return new PeliculaDTO(
                pelicula.getId(),
                pelicula.getTitulo(),
                pelicula.getDuracion(),
                pelicula.getFechaEstreno(),
                pelicula.getSinopsis(),
                pelicula.getValoracion()
        );
    }


    public PeliculaDTO buscarPorId(Long id) {
        for (PeliculaDTO p : listar()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
        /*
        * return peliculas.stream()                 // convierte la lista en un flujo de datos
        .filter(p -> p.getId().equals(id)) // se queda solo con las películas cuyo id coincide
        .findFirst()                       // toma la primera coincidencia (si existe)
        .orElse(null);                     // devuelve esa película o null si no hay
        * */
    }

    public void agregar(Pelicula pelicula) {
        peliculas.add(pelicula);
    }


    // Ejercicio mandado en clase para devolver las películas con mejor puntuación
    public List<PeliculaDTO> devolverPeliculasPuntuacion(int puntuacionMinima){

        List<PeliculaDTO> peliculasFiltradas = new ArrayList<>();
        for(PeliculaDTO pelicula : this.listar()){
            if (pelicula.getValoracion() >= puntuacionMinima) peliculasFiltradas.add(pelicula);
        }
        return peliculasFiltradas;
    }




}
