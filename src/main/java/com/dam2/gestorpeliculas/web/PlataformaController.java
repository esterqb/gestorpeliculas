package com.dam2.gestorpeliculas.web;

import com.dam2.gestorpeliculas.DTO.PlataformaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.PlataformaDTO;
import com.dam2.gestorpeliculas.service.PlataformaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plataformas")
public class PlataformaController {

    private final PlataformaService service;

    public PlataformaController(PlataformaService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlataformaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlataformaDTO> buscar(@PathVariable Long id) {
        PlataformaDTO dto = service.buscar(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PlataformaDTO> crear(@RequestBody PlataformaCreateUpdateDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlataformaDTO> actualizar(@PathVariable Long id, @RequestBody PlataformaCreateUpdateDTO dto) {
        PlataformaDTO actualizado = service.actualizar(id, dto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        return service.borrar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
