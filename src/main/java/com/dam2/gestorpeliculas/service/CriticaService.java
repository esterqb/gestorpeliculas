package com.dam2.gestorpeliculas.service;

import com.dam2.gestorpeliculas.DTO.CriticaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.CriticaDTO;
import com.dam2.gestorpeliculas.domain.Critica;
import com.dam2.gestorpeliculas.domain.Pelicula;
import com.dam2.gestorpeliculas.domain.Usuario;
import com.dam2.gestorpeliculas.repository.CriticaRepository;
import com.dam2.gestorpeliculas.repository.PeliculaRepository;
import com.dam2.gestorpeliculas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CriticaService {

    private final CriticaRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final PeliculaRepository peliculaRepo;

    public CriticaService(CriticaRepository repo, UsuarioRepository usuarioRepo, PeliculaRepository peliculaRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
        this.peliculaRepo = peliculaRepo;
    }

    private CriticaDTO toDTO(Critica c) {
        return new CriticaDTO(
                c.getId(),
                c.getComentario(),
                c.getNota(),
                c.getFecha(),
                c.getUsuario() != null ? c.getUsuario().getId() : null,
                c.getPelicula() != null ? c.getPelicula().getId() : null
        );
    }

    public List<CriticaDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CriticaDTO buscar(Long id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public CriticaDTO crear(CriticaCreateUpdateDTO dto) {
        Critica c = new Critica();
        c.setComentario(dto.getComentario());
        c.setNota(dto.getNota());
        c.setFecha(dto.getFecha());

        Usuario u = usuarioRepo.findById(dto.getUsuarioId()).orElse(null);
        Pelicula p = peliculaRepo.findById(dto.getPeliculaId()).orElse(null);
        c.setUsuario(u);
        c.setPelicula(p);

        return toDTO(repo.save(c));
    }

    public CriticaDTO actualizar(Long id, CriticaCreateUpdateDTO dto) {
        return repo.findById(id).map(c -> {
            c.setComentario(dto.getComentario());
            c.setNota(dto.getNota());
            c.setFecha(dto.getFecha());
            Usuario u = usuarioRepo.findById(dto.getUsuarioId()).orElse(null);
            Pelicula p = peliculaRepo.findById(dto.getPeliculaId()).orElse(null);
            c.setUsuario(u);
            c.setPelicula(p);
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
