package web;


import domain.Pelicula;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import service.PeliculaService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/peliculas")
@RequiredArgsConstructor
public class PeliculaController {
    private final PeliculaService peliculaService;


    //EJERCICIO 1: eject
    //http://localhost:8081/api/peliculas/sync
    @GetMapping("/sync")
    public String pruebaSync() {

        long inicio = System.currentTimeMillis();

        peliculaService.tareaLenta("Interstellar");
        peliculaService.tareaLenta("The Dark Knight");
        peliculaService.tareaLenta("Soul");

        long fin = System.currentTimeMillis();

        return "Tiempo total -sync-: " + (fin - inicio) + "ms";
    }

    //http://localhost:8081/api/peliculas/async
    @GetMapping("/async")
    public String pruebaAsync() {

        long inicio = System.currentTimeMillis();

        CompletableFuture<String> t1 = peliculaService.tareaLenta2("Interstellar");
        CompletableFuture<String> t2 = peliculaService.tareaLenta2("The Dark Knight");
        CompletableFuture<String> t3 = peliculaService.tareaLenta2("Soul");

        // PUNTO 6: esperar a que terminen
        CompletableFuture.allOf(t1, t2, t3).join();

        long fin = System.currentTimeMillis();

        return "Tiempo total -async-: " + (fin - inicio) + "ms";
    }

    //EJERCICIO 2: lanzar varias
    @GetMapping("/reproducir")
    public String reproducirVarias() {
        long inicio = System.currentTimeMillis();

        CompletableFuture<String> t1 = peliculaService.reproducir("Interstellar");
        CompletableFuture<String> t2 = peliculaService.reproducir("The Dark Knight");
        CompletableFuture<String> t3 = peliculaService.reproducir("Soul");

        CompletableFuture.allOf(t1, t2, t3).join();

        long fin = System.currentTimeMillis();

        return "Tiempo total reproducción: " + (fin - inicio) + " ms";
    }

    @GetMapping
    public List<Pelicula> listar() {
        return peliculaService.listar();
    }

    @GetMapping("/{id}")
    public Pelicula buscarPorId(@PathVariable Long id) {
        return peliculaService.buscarPorId(id);
    }

    @GetMapping("/peliculas_mejores")
    public List<Pelicula> mejores_peliculas() {
        return peliculaService.mejores_peliculas(5);
    }
    /*
        @GetMapping("/mejores_peliculas")
        public List<Pelicula> mejores_peliculas() {
            return service.mejores_peliculas();
        }
    */
    @PostMapping
    public void agregar(@RequestBody Pelicula pelicula) {
        peliculaService.agregar(pelicula);
    }

    @GetMapping("/procesar")
    public String procesarPeliculas() {
        long inicio = System.currentTimeMillis();
        peliculaService.tareaLenta("Interstellar");
        peliculaService.tareaLenta("The Dark Knight");
        peliculaService.tareaLenta("Soul");
        long fin = System.currentTimeMillis();
        return "Tiempo total: " + (fin - inicio) + " ms";
    }

    //http://localhost:8081/api/peliculas/procesarAsync
    @GetMapping("/procesarAsync")
    public String procesar_async() {
        long inicio = System.currentTimeMillis();

        var t1 = peliculaService.tareaLenta2("🍿 Interstellar");
        var t2 = peliculaService.tareaLenta2("🦇 The Dark Knight");
        var t3 = peliculaService.tareaLenta2("🎵 Soul");
        var t4 = peliculaService.tareaLenta2("🎵 Soul");
        var t5 = peliculaService.tareaLenta2("🎵 Soul");
        var t6 = peliculaService.tareaLenta2("🎵 Soul");
        //var t7 = service.tareaLenta2("🎵 Soul");

        // Espera a que terminen todas las tareas
        CompletableFuture.allOf(t1, t2, t3,t4,t5,t6).join();

        long fin = System.currentTimeMillis();
        return "Tiempo total (asíncrono): " + (fin - inicio) + " ms";
    }

    // A4 - Ejercicio 2
    //http://localhost:8081/api/peliculas/reproducir_async
    @GetMapping("/reproducir_async")
    public String reproducirAsync() {
        long inicio = System.currentTimeMillis();

        var t1 = peliculaService.reproducir("🍿 Interstellar");
        var t2 = peliculaService.reproducir("🦇 The Dark Knight");
        var t3 = peliculaService.reproducir("🎵 Soul");

        // Espera a que terminen todas las tareas
        CompletableFuture.allOf(t1, t2, t3).join();

        long fin = System.currentTimeMillis();
        return "Tiempo total (asíncrono): " + (fin - inicio) + " ms";
    }

    //Ejercicio 4-
    @GetMapping("/oscar")
    public String oscar() {
        return peliculaService.votarOscar();
    }

}
