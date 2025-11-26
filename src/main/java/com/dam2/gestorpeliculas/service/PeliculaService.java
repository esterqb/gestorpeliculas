package com.dam2.gestorpeliculas.service;


import com.dam2.gestorpeliculas.DTO.PeliculaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.PeliculaDTO;
import com.dam2.gestorpeliculas.domain.*;
import com.dam2.gestorpeliculas.repository.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.stream.Collectors;


@Service
@Getter
public class PeliculaService {
    private final DirectorRepository directorRepo;
    private final FichaTecnicaRepository fichaRepo;
    private final ActorRepository actorRepo;
    private final PlataformaRepository plataformaRepo;
    private final IdiomaRepository idiomaRepo;
    private final CategoriaRepository categoriaRepo;
    @Autowired
    private PeliculaRepository peliculaRepository;


    public PeliculaService(PeliculaRepository peliculaRepository, DirectorRepository directorRepo, FichaTecnicaRepository fichaRepo, ActorRepository actorRepo, PlataformaRepository plataformaRepo, IdiomaRepository idiomaRepo, CategoriaRepository categoriaRepo) {
        this.peliculaRepository = peliculaRepository;
        this.directorRepo = directorRepo;
        this.fichaRepo = fichaRepo;
        this.actorRepo = actorRepo;
        this.plataformaRepo = plataformaRepo;
        this.idiomaRepo = idiomaRepo;
        this.categoriaRepo = categoriaRepo;
    }

    public List<PeliculaDTO> listar() {
        return peliculaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        //pasar por dto
    }
    private PeliculaDTO toDTO(Pelicula p) {
        return new PeliculaDTO(
                p.getId(),
                p.getTitulo(),
                p.getDuracion(),
                p.getFechaEstreno(),
                p.getSinopsis(),
                p.getValoracion(),
                p.getFichaTecnica() != null ? p.getFichaTecnica().getId() : null,
                p.getDirector() != null ? p.getDirector().getId() : null,
                p.getActores().stream().map(Actor::getId).toList(),
                p.getPlataformas().stream().map(Plataforma::getId).toList(),
                p.getIdiomas().stream().map(Idioma::getId).toList(),
                p.getCategorias().stream().map(Categoria::getId).toList()
        );
    }

    public PeliculaDTO buscarPorId(Long id) {
        return peliculaRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }


    public Pelicula agregar(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    public List<PeliculaDTO> devolverPeliculasPuntuacion(int puntuacionMinima){

        List<PeliculaDTO> peliculasFiltradas = new ArrayList<>();
        for(PeliculaDTO pelicula : this.listar()){
            if (pelicula.getValoracion() >= puntuacionMinima) peliculasFiltradas.add(pelicula);
        }
        return peliculasFiltradas;
    }


    public PeliculaDTO crear(PeliculaCreateUpdateDTO dto) {
        Pelicula p = new Pelicula();
        p.setTitulo(dto.getTitulo());
        p.setDuracion(dto.getDuracion());
        p.setFechaEstreno(dto.getFechaEstreno());
        p.setSinopsis(dto.getSinopsis());
        p.setValoracion(dto.getValoracion());

        // Relaciones
        fichaRepo.findById(dto.getFichaTecnicaId()).ifPresent(p::setFichaTecnica);
        directorRepo.findById(dto.getDirectorId()).ifPresent(p::setDirector);

        if (dto.getActoresIds() != null) {
            dto.getActoresIds().forEach(id -> actorRepo.findById(id).ifPresent(p::addActor));
        }

        if (dto.getPlataformasIds() != null) {
            dto.getPlataformasIds().forEach(id -> plataformaRepo.findById(id).ifPresent(p.getPlataformas()::add));
        }

        if (dto.getIdiomasIds() != null) {
            dto.getIdiomasIds().forEach(id -> idiomaRepo.findById(id).ifPresent(p.getIdiomas()::add));
        }

        if (dto.getCategoriasIds() != null) {
            dto.getCategoriasIds().forEach(id -> categoriaRepo.findById(id).ifPresent(p.getCategorias()::add));
        }

        return toDTO(peliculaRepository.save(p));
    }

    public PeliculaDTO actualizar(Long id, PeliculaCreateUpdateDTO dto) {
        return peliculaRepository.findById(id).map(p -> {
            p.setTitulo(dto.getTitulo());
            p.setDuracion(dto.getDuracion());
            p.setFechaEstreno(dto.getFechaEstreno());
            p.setSinopsis(dto.getSinopsis());
            p.setValoracion(dto.getValoracion());

            // Relaciones
            fichaRepo.findById(dto.getFichaTecnicaId()).ifPresent(p::setFichaTecnica);
            directorRepo.findById(dto.getDirectorId()).ifPresent(p::setDirector);

            p.getActores().clear();
            if (dto.getActoresIds() != null) {
                dto.getActoresIds().forEach(aid -> actorRepo.findById(aid).ifPresent(p::addActor));
            }

            p.getPlataformas().clear();
            if (dto.getPlataformasIds() != null) {
                dto.getPlataformasIds().forEach(pid -> plataformaRepo.findById(pid).ifPresent(p.getPlataformas()::add));
            }

            p.getIdiomas().clear();
            if (dto.getIdiomasIds() != null) {
                dto.getIdiomasIds().forEach(iid -> idiomaRepo.findById(iid).ifPresent(p.getIdiomas()::add));
            }

            p.getCategorias().clear();
            if (dto.getCategoriasIds() != null) {
                dto.getCategoriasIds().forEach(cid -> categoriaRepo.findById(cid).ifPresent(p.getCategorias()::add));
            }

            return toDTO(peliculaRepository.save(p));
        }).orElse(null);
    }

    public boolean borrar(Long id) {
        return peliculaRepository.findById(id).map(p -> {
            peliculaRepository.delete(p);
            return true;
        }).orElse(false);
    }
}