package com.dam2.gestorpeliculas.repository;

import com.dam2.gestorpeliculas.domain.Director;
import com.dam2.gestorpeliculas.domain.FichaTecnica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FichaTecnicaRepository extends JpaRepository<FichaTecnica, Long> {
    List<FichaTecnica> findByDirector(Director director);
}
