package com.dam2.gestorpeliculas.web;

import com.dam2.gestorpeliculas.Async.PeliculaAsyncService;
import com.dam2.gestorpeliculas.DTO.PeliculaCreateUpdateDTO;
import com.dam2.gestorpeliculas.DTO.PeliculaDTO;
import com.dam2.gestorpeliculas.domain.Pelicula;
import com.dam2.gestorpeliculas.service.PeliculaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;
    private final PeliculaAsyncService peliculaAsyncService;

    public PeliculaController(PeliculaService peliculaService, PeliculaAsyncService peliculaAsyncService) {
        this.peliculaService = peliculaService;
        this.peliculaAsyncService = peliculaAsyncService;
    }

    // Listar todas las películas
    @GetMapping
    public List<PeliculaDTO> listar() {
        return peliculaService.listar();
    }

    // Buscar película por ID
    @GetMapping("/{id}")
    public PeliculaDTO buscarPorId(@PathVariable Long id) {
        return peliculaService.buscarPorId(id);
    }

    // Ejercicio Sync
    @GetMapping("/sync")
    public String pruebaSync() {
        long inicio = System.currentTimeMillis();

        peliculaAsyncService.tareaLenta("Interstellar");
        peliculaAsyncService.tareaLenta("The Dark Knight");
        peliculaAsyncService.tareaLenta("Soul");

        long fin = System.currentTimeMillis();
        return "Tiempo total -sync-: " + (fin - inicio) + " ms";
    }

    // Ejercicio Async
    @GetMapping("/async")
    public String pruebaAsync() {
        long inicio = System.currentTimeMillis();

        CompletableFuture<String> t1 = peliculaAsyncService.tareaLenta2("Interstellar");
        CompletableFuture<String> t2 = peliculaAsyncService.tareaLenta2("The Dark Knight");
        CompletableFuture<String> t3 = peliculaAsyncService.tareaLenta2("Soul");

        CompletableFuture.allOf(t1, t2, t3).join();

        long fin = System.currentTimeMillis();
        return "Tiempo total -async-: " + (fin - inicio) + " ms";
    }

    // Agregar nueva película
//    @PostMapping
//    public void agregar(@RequestBody Pelicula pelicula) {
//        peliculaService.agregar(pelicula);
//    }
    @PostMapping
    public ResponseEntity<PeliculaDTO> crear(@RequestBody PeliculaCreateUpdateDTO dto) {
        return ResponseEntity.ok(peliculaService.crear(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PeliculaDTO> actualizar(@PathVariable Long id, @RequestBody PeliculaCreateUpdateDTO dto) {
        PeliculaDTO actualizado = peliculaService.actualizar(id, dto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        return peliculaService.borrar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Películas mejores
    @GetMapping("/peliculas_mejores")
    public List<PeliculaDTO> mejoresPeliculas() {
        return peliculaService.devolverPeliculasPuntuacion(5);
    }

    // Votar Oscar
    @GetMapping("/oscar")
    public HashMap<String, Integer> oscar() {
        return peliculaAsyncService.simularVotacionesAleatorias(50); //num de votaciones
    }


    //Reproducir varias películas async
    @GetMapping("/reproducir_async")
    public String reproducirAsync() {
        long inicio = System.currentTimeMillis();

        var t1 = peliculaAsyncService.reproducir("Interstellar");
        var t2 = peliculaAsyncService.reproducir("The Dark Knight");
        var t3 = peliculaAsyncService.reproducir("Soul");

        CompletableFuture.allOf(t1, t2, t3).join();

        long fin = System.currentTimeMillis();
        return "Tiempo total reproducción (async): " + (fin - inicio) + " ms";
    }
}
