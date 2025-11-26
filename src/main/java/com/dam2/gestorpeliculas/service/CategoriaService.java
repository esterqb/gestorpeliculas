package com.dam2.gestorpeliculas.service;

import com.dam2.gestorpeliculas.DTO.CategoriaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.CategoriaDTO;
import com.dam2.gestorpeliculas.domain.Categoria;
import com.dam2.gestorpeliculas.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    private CategoriaDTO toDTO(Categoria c) {
        return new CategoriaDTO(c.getId(), c.getNombre());
    }

    public List<CategoriaDTO> listar() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO buscar(Long id) {
        return repo.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    public CategoriaDTO crear(CategoriaCreateUpdateDTO dto) {
        Categoria c = new Categoria();
        c.setNombre(dto.getNombre());
        return toDTO(repo.save(c));
    }

    public CategoriaDTO actualizar(Long id, CategoriaCreateUpdateDTO dto) {
        return repo.findById(id).map(c -> {
            c.setNombre(dto.getNombre());
            return toDTO(repo.save(c));
        }).orElse(null);
    }

    public boolean borrar(Long id) {
        return repo.findById(id).map(c -> {
            repo.delete(c);
            return true;
        }).orElse(false);
    }
}
