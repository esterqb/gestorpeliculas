package com.dam2.gestorpeliculas.config;

import com.dam2.gestorpeliculas.domain.*;
import com.dam2.gestorpeliculas.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ActorRepository actorRepo;
    private final DirectorRepository directorRepo;
    private final PeliculaRepository peliculaRepo;

    @Override
    @Transactional
    public void run(String... args) {

        System.out.println("Cargando datos de prueba...");

        // DIRECTORES
        Director nolan = directorRepo.findByNombre("Christopher Nolan")
                .orElseGet(() -> directorRepo.save(new Director(null, "Christopher Nolan", new ArrayList<>())));

        Director docter = directorRepo.findByNombre("Pete Docter")
                .orElseGet(() -> directorRepo.save(new Director(null, "Pete Docter", new ArrayList<>())));

        // PELÍCULAS
        Pelicula interstellar = peliculaRepo.findByTitulo("Interstellar")
                .orElseGet(() -> {
                    Pelicula p = new Pelicula();
                    p.setTitulo("Interstellar");
                    p.setDuracion(169);
                    p.setFechaEstreno(LocalDate.of(2014, 11, 7));
                    p.setSinopsis("Exploradores espaciales viajan...");
                    p.setValoracion(9);
                    p.setDirector(nolan);
                    return peliculaRepo.save(p);
                });

        Pelicula soul = peliculaRepo.findByTitulo("Soul")
                .orElseGet(() -> {
                    Pelicula p = new Pelicula();
                    p.setTitulo("Soul");
                    p.setDuracion(100);
                    p.setFechaEstreno(LocalDate.of(2020, 12, 25));
                    p.setSinopsis("Un músico descubre la vida...");
                    p.setValoracion(8);
                    p.setDirector(docter);
                    return peliculaRepo.save(p);
                });

        // ACTORES
        Actor matthew = actorRepo.findByNombre("Matthew McConaughey")
                .orElseGet(() -> actorRepo.save(new Actor(null, "Matthew McConaughey", new ArrayList<>())));

        Actor hathaway = actorRepo.findByNombre("Anne Hathaway")
                .orElseGet(() -> actorRepo.save(new Actor(null, "Anne Hathaway", new ArrayList<>())));

        Actor foxx = actorRepo.findByNombre("Jamie Foxx")
                .orElseGet(() -> actorRepo.save(new Actor(null, "Jamie Foxx", new ArrayList<>())));

        // RELACIONES
        interstellar.getActores().clear();
        interstellar.addActor(matthew);
        interstellar.addActor(hathaway);

        soul.getActores().clear();
        soul.addActor(foxx);


        peliculaRepo.save(interstellar);
        peliculaRepo.save(soul);

        System.out.println(">>> DATOS DE PRUEBA INSERTADOS CORRECTAMENTE");
    }
}
