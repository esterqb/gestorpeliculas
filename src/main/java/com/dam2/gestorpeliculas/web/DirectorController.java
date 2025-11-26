package com.dam2.gestorpeliculas.web;

import com.dam2.gestorpeliculas.DTO.DirectorCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.DirectorDTO;
import com.dam2.gestorpeliculas.service.DirectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directores")
public class DirectorController {

    private final DirectorService service;

    public DirectorController(DirectorService service) {
        this.service = service;
    }

    @GetMapping
    public List<DirectorDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorDTO> buscarPorId(@PathVariable Long id) {
        DirectorDTO dto = service.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<DirectorDTO> crear(@RequestBody DirectorCreateUpdateDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DirectorDTO> actualizar(@PathVariable Long id, @RequestBody DirectorCreateUpdateDTO dto) {
        DirectorDTO actualizado = service.actualizar(id, dto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        return service.borrar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
