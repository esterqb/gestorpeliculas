package com.dam2.gestorpeliculas.web;

import com.dam2.gestorpeliculas.DTO.CriticaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.CriticaDTO;
import com.dam2.gestorpeliculas.service.CriticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criticas")
public class CriticaController {

    private final CriticaService service;

    public CriticaController(CriticaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CriticaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CriticaDTO> buscar(@PathVariable Long id) {
        CriticaDTO dto = service.buscar(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CriticaDTO> crear(@RequestBody CriticaCreateUpdateDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CriticaDTO> actualizar(@PathVariable Long id, @RequestBody CriticaCreateUpdateDTO dto) {
        CriticaDTO actualizado = service.actualizar(id, dto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        return service.borrar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
