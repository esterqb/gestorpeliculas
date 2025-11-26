package com.dam2.gestorpeliculas.web;

import com.dam2.gestorpeliculas.DTO.FichaTecnicaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.FichaTecnicaDTO;
import com.dam2.gestorpeliculas.service.FichaTecnicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichas-tecnicas")
public class FichaTecnicaController {

    private final FichaTecnicaService service;

    public FichaTecnicaController(FichaTecnicaService service) {
        this.service = service;
    }

    @GetMapping
    public List<FichaTecnicaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichaTecnicaDTO> buscar(@PathVariable Long id) {
        FichaTecnicaDTO dto = service.buscar(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FichaTecnicaDTO> crear(@RequestBody FichaTecnicaCreateUpdateDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FichaTecnicaDTO> actualizar(@PathVariable Long id, @RequestBody FichaTecnicaCreateUpdateDTO dto) {
        FichaTecnicaDTO updated = service.actualizar(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        return service.borrar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
