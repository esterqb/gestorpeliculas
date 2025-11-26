package com.dam2.gestorpeliculas.service;

import com.dam2.gestorpeliculas.DTO.ActorCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.ActorDTO;
import com.dam2.gestorpeliculas.domain.Actor;
import com.dam2.gestorpeliculas.repository.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<ActorDTO> listar() {
        return actorRepository.findAll()
                .stream()
                .map(a -> new ActorDTO(a.getId(), a.getNombre()))
                .collect(Collectors.toList());
    }

    public ActorDTO buscarPorId(Long id) {
        return actorRepository.findById(id)
                .map(a -> new ActorDTO(a.getId(), a.getNombre()))
                .orElse(null);
    }

    public ActorDTO crear(ActorCreateUpdateDTO dto) {
        Actor a = new Actor();
        a.setNombre(dto.getNombre());
        Actor saved = actorRepository.save(a);
        return new ActorDTO(saved.getId(), saved.getNombre());
    }

    public ActorDTO actualizar(Long id, ActorCreateUpdateDTO dto) {
        return actorRepository.findById(id).map(a -> {
            a.setNombre(dto.getNombre());
            Actor saved = actorRepository.save(a);
            return new ActorDTO(saved.getId(), saved.getNombre());
        }).orElse(null);
    }

    public boolean borrar(Long id) {
        return actorRepository.findById(id)
                .map(a -> {
                    actorRepository.delete(a);
                    return true;
                }).orElse(false);
    }
}
