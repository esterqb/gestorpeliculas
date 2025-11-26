package com.dam2.gestorpeliculas.web;

import com.dam2.gestorpeliculas.DTO.ActorCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.ActorDTO;
import com.dam2.gestorpeliculas.service.ActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actores")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public List<ActorDTO> listar() {
        return actorService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> buscarPorId(@PathVariable Long id) {
        ActorDTO dto = actorService.buscarPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ActorDTO> crear(@RequestBody ActorCreateUpdateDTO dto) {
        return ResponseEntity.ok(actorService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorDTO> actualizar(@PathVariable Long id, @RequestBody ActorCreateUpdateDTO dto) {
        ActorDTO actualizado = actorService.actualizar(id, dto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        return actorService.borrar(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}
