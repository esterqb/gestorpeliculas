package com.dam2.gestorpeliculas.repository;

import com.dam2.gestorpeliculas.domain.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    Optional<Pelicula> findByTitulo(String titulo);
}