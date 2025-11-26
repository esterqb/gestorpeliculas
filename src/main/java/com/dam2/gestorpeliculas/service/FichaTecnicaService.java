package com.dam2.gestorpeliculas.service;

import com.dam2.gestorpeliculas.DTO.FichaTecnicaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.FichaTecnicaDTO;
import com.dam2.gestorpeliculas.domain.Director;
import com.dam2.gestorpeliculas.domain.FichaTecnica;
import com.dam2.gestorpeliculas.repository.DirectorRepository;
import com.dam2.gestorpeliculas.repository.FichaTecnicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FichaTecnicaService {

    private final FichaTecnicaRepository repo;
    private final DirectorRepository directorRepo;

    public FichaTecnicaService(FichaTecnicaRepository repo, DirectorRepository directorRepo) {
        this.repo = repo;
        this.directorRepo = directorRepo;
    }

    private FichaTecnicaDTO toDTO(FichaTecnica f) {
        return new FichaTecnicaDTO(
                f.getId(),
                f.getDuracion(),
                f.getPais(),
                f.getDirector() != null ? f.getDirector().getId() : null
        );
    }

    public List<FichaTecnicaDTO> listar() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public FichaTecnicaDTO buscar(Long id) {
        return repo.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    public FichaTecnicaDTO crear(FichaTecnicaCreateUpdateDTO dto) {

        Director director = null;
        if (dto.getDirectorId() != null) {
            director = directorRepo.findById(dto.getDirectorId()).orElse(null);
        }

        FichaTecnica f = new FichaTecnica();
        f.setDuracion(dto.getDuracion());
        f.setPais(dto.getPais());
        f.setDirector(director);

        return toDTO(repo.save(f));
    }

    public FichaTecnicaDTO actualizar(Long id, FichaTecnicaCreateUpdateDTO dto) {

        return repo.findById(id).map(f -> {

            Director director = null;
            if (dto.getDirectorId() != null) {
                director = directorRepo.findById(dto.getDirectorId()).orElse(null);
            }

            f.setDuracion(dto.getDuracion());
            f.setPais(dto.getPais());
            f.setDirector(director);

            return toDTO(repo.save(f));

        }).orElse(null);
    }

    public boolean borrar(Long id) {
        return repo.findById(id).map(f -> {
            repo.delete(f);
            return true;
        }).orElse(false);
    }
}
