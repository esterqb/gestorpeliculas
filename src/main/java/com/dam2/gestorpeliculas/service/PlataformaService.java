package com.dam2.gestorpeliculas.service;

import com.dam2.gestorpeliculas.DTO.PlataformaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.PlataformaDTO;
import com.dam2.gestorpeliculas.domain.Plataforma;
import com.dam2.gestorpeliculas.repository.PlataformaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlataformaService {

    private final PlataformaRepository repo;

    public PlataformaService(PlataformaRepository repo) {
        this.repo = repo;
    }

    private PlataformaDTO toDTO(Plataforma p) {
        return new PlataformaDTO(p.getId(), p.getNombre(), p.getUrl());
    }

    public List<PlataformaDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PlataformaDTO buscar(Long id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public PlataformaDTO crear(PlataformaCreateUpdateDTO dto) {
        Plataforma p = new Plataforma();
        p.setNombre(dto.getNombre());
        p.setUrl(dto.getUrl());
        return toDTO(repo.save(p));
    }

    public PlataformaDTO actualizar(Long id, PlataformaCreateUpdateDTO dto) {
        return repo.findById(id).map(p -> {
            p.setNombre(dto.getNombre());
            p.setUrl(dto.getUrl());
            return toDTO(repo.save(p));
        }).orElse(null);
    }

    public boolean borrar(Long id) {
        return repo.findById(id).map(p -> {
            repo.delete(p);
            return true;
        }).orElse(false);
    }
}
