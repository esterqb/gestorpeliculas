package com.dam2.gestorpeliculas.service;

import com.dam2.gestorpeliculas.DTO.DirectorCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.DirectorDTO;
import com.dam2.gestorpeliculas.domain.Director;
import com.dam2.gestorpeliculas.repository.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectorService {

    private final DirectorRepository repo;

    public DirectorService(DirectorRepository repo) {
        this.repo = repo;
    }

    public List<DirectorDTO> listar() {
        return repo.findAll()
                .stream()
                .map(d -> new DirectorDTO(d.getId(), d.getNombre()))
                .collect(Collectors.toList());
    }

    public DirectorDTO buscarPorId(Long id) {
        return repo.findById(id)
                .map(d -> new DirectorDTO(d.getId(), d.getNombre()))
                .orElse(null);
    }

    public DirectorDTO crear(DirectorCreateUpdateDTO dto) {
        Director d = new Director();
        d.setNombre(dto.getNombre());
        Director saved = repo.save(d);
        return new DirectorDTO(saved.getId(), saved.getNombre());
    }

    public DirectorDTO actualizar(Long id, DirectorCreateUpdateDTO dto) {
        return repo.findById(id)
                .map(d -> {
                    d.setNombre(dto.getNombre());
                    Director saved = repo.save(d);
                    return new DirectorDTO(saved.getId(), saved.getNombre());
                }).orElse(null);
    }

    public boolean borrar(Long id) {
        return repo.findById(id)
                .map(d -> {
                    repo.delete(d);
                    return true;
                }).orElse(false);
    }
}
