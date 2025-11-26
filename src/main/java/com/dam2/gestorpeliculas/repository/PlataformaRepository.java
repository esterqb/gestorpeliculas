package com.dam2.gestorpeliculas.repository;

import com.dam2.gestorpeliculas.domain.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {
}
