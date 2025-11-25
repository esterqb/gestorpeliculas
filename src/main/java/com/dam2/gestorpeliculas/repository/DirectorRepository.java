package com.dam2.gestorpeliculas.repository;

import com.dam2.gestorpeliculas.domain.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findByNombre(String nombre);
}
