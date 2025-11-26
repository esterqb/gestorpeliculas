package com.dam2.gestorpeliculas.service;

import com.dam2.gestorpeliculas.DTO.IdiomaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.IdiomaDTO;
import com.dam2.gestorpeliculas.domain.Idioma;
import com.dam2.gestorpeliculas.repository.IdiomaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IdiomaService {

    private final IdiomaRepository repo;

    public IdiomaService(IdiomaRepository repo) {
        this.repo = repo;
    }

    private IdiomaDTO toDTO(Idioma i) {
        return new IdiomaDTO(i.getId(), i.getNombre());
    }

    public List<IdiomaDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public IdiomaDTO buscar(Long id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public IdiomaDTO crear(IdiomaCreateUpdateDTO dto) {
        Idioma i = new Idioma();
        i.setNombre(dto.getNombre());
        return toDTO(repo.save(i));
    }

    public IdiomaDTO actualizar(Long id, IdiomaCreateUpdateDTO dto) {
        return repo.findById(id).map(i -> {
            i.setNombre(dto.getNombre());
            return toDTO(repo.save(i));
        }).orElse(null);
    }

    public boolean borrar(Long id) {
        return repo.findById(id).map(i -> {
            repo.delete(i);
            return true;
        }).orElse(false);
    }
}
