package com.dam2.gestorpeliculas.web;

import com.dam2.gestorpeliculas.DTO.IdiomaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.IdiomaDTO;
import com.dam2.gestorpeliculas.service.IdiomaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/idiomas")
public class IdiomaController {

    private final IdiomaService service;

    public IdiomaController(IdiomaService service) {
        this.service = service;
    }

    @GetMapping
    public List<IdiomaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdiomaDTO> buscar(@PathVariable Long id) {
        IdiomaDTO dto = service.buscar(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<IdiomaDTO> crear(@RequestBody IdiomaCreateUpdateDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdiomaDTO> actualizar(@PathVariable Long id, @RequestBody IdiomaCreateUpdateDTO dto) {
        IdiomaDTO actualizado = service.actualizar(id, dto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        return service.borrar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
