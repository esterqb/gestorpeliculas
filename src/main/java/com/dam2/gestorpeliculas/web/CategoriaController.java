package com.dam2.gestorpeliculas.web;

import com.dam2.gestorpeliculas.DTO.CategoriaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.CategoriaDTO;
import com.dam2.gestorpeliculas.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
// 🚀 AÑADIDO CLAVE: Permite peticiones desde tu frontend de React (u otro origen)
@CrossOrigin(origins = "http://localhost:5173")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscar(@PathVariable Long id) {
        CategoriaDTO dto = service.buscar(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@RequestBody CategoriaCreateUpdateDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable Long id, @RequestBody CategoriaCreateUpdateDTO dto) {
        CategoriaDTO actualizado = service.actualizar(id, dto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        return service.borrar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}