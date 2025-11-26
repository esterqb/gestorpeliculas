package com.dam2.gestorpeliculas.service;

import com.dam2.gestorpeliculas.DTO.UsuarioCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.UsuarioDTO;
import com.dam2.gestorpeliculas.domain.Usuario;
import com.dam2.gestorpeliculas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getUsername(), u.getEmail(), u.getRol());
    }

    public List<UsuarioDTO> listar() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public UsuarioDTO buscar(Long id) {
        return repo.findById(id).map(this::toDTO).orElse(null);
    }

    public UsuarioDTO crear(UsuarioCreateUpdateDTO dto) {
        Usuario u = new Usuario();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setRol(dto.getRol());
        return toDTO(repo.save(u));
    }

    public UsuarioDTO actualizar(Long id, UsuarioCreateUpdateDTO dto) {
        return repo.findById(id).map(u -> {
            u.setUsername(dto.getUsername());
            u.setEmail(dto.getEmail());
            u.setPassword(dto.getPassword());
            u.setRol(dto.getRol());
            return toDTO(repo.save(u));
        }).orElse(null);
    }

    public boolean borrar(Long id) {
        return repo.findById(id).map(u -> {
            repo.delete(u);
            return true;
        }).orElse(false);
    }
}
