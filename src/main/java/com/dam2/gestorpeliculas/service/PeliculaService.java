package com.dam2.gestorpeliculas.service;


import com.dam2.gestorpeliculas.DTO.PeliculaDTO;
import com.dam2.gestorpeliculas.domain.Pelicula;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

import com.dam2.gestorpeliculas.repository.PeliculaRepository;


@Service
@Getter
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;


    public PeliculaService(PeliculaRepository peliculaRepository) {
        this.peliculaRepository = peliculaRepository;
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
        return peliculaRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }


    public Pelicula agregar(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
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
