package com.dam2.gestorpeliculas.config;

import com.dam2.gestorpeliculas.domain.Actor;
import com.dam2.gestorpeliculas.domain.Director;
import com.dam2.gestorpeliculas.domain.FichaTecnica;
import com.dam2.gestorpeliculas.domain.Pelicula;
import com.dam2.gestorpeliculas.repository.ActorRepository;
import com.dam2.gestorpeliculas.repository.DirectorRepository;
import com.dam2.gestorpeliculas.repository.FichaTecnicaRepository;
import com.dam2.gestorpeliculas.repository.PeliculaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(
            ActorRepository actorRepo,
            DirectorRepository directorRepo,
            FichaTecnicaRepository fichaRepo,
            PeliculaRepository peliculaRepo) {

        return args -> {
            System.out.println("Cargando datos de prueba...");

            // ----------------------
            // DIRECTORES
            // ----------------------
            Director nolan = directorRepo.findByNombre("Christopher Nolan")
                    .orElseGet(() -> directorRepo.save(new Director(null, "Christopher Nolan", new ArrayList<>())));

            Director docter = directorRepo.findByNombre("Pete Docter")
                    .orElseGet(() -> directorRepo.save(new Director(null, "Pete Docter", new ArrayList<>())));

            // ----------------------
            // FICHAS TÉCNICAS
            // ----------------------
            FichaTecnica f1 = fichaRepo.findByDirector(nolan)
                    .stream().findFirst()
                    .orElseGet(() -> fichaRepo.save(new FichaTecnica(null, nolan, 169, "EE.UU.")));

            FichaTecnica f2 = fichaRepo.findByDirector(docter)
                    .stream().findFirst()
                    .orElseGet(() -> fichaRepo.save(new FichaTecnica(null, docter, 100, "EE.UU.")));

            // ----------------------
            // PELÍCULAS
            // ----------------------
            Pelicula interstellar = peliculaRepo.findByTitulo("Interstellar")
                    .orElseGet(() -> {
                        Pelicula p = new Pelicula();
                        p.setTitulo("Interstellar");
                        p.setDuracion(169);
                        p.setFechaEstreno(LocalDate.of(2014, 11, 7));
                        p.setSinopsis("Exploradores espaciales viajan a través de un agujero de gusano...");
                        p.setValoracion(9);
                        p.setFichaTecnica(f1);
                        p.setDirector(nolan);
                        return peliculaRepo.save(p);
                    });


            Pelicula soul = peliculaRepo.findByTitulo("Soul")
                    .orElseGet(() -> {
                        Pelicula p = new Pelicula();
                        p.setTitulo("Soul");
                        p.setDuracion(100);
                        p.setFechaEstreno(LocalDate.of(2020, 12, 25));
                        p.setSinopsis("Un músico descubre el verdadero sentido de la vida...");
                        p.setValoracion(8);
                        p.setFichaTecnica(f2);
                        p.setDirector(docter);
                        return peliculaRepo.save(p);
                    });


            // ----------------------
            // ACTORES
            // ----------------------
            Actor matthew = actorRepo.findByNombre("Matthew McConaughey")
                    .orElseGet(() -> actorRepo.save(new Actor(null, "Matthew McConaughey", new ArrayList<>())));

            Actor hathaway = actorRepo.findByNombre("Anne Hathaway")
                    .orElseGet(() -> actorRepo.save(new Actor(null, "Anne Hathaway", new ArrayList<>())));

            Actor foxx = actorRepo.findByNombre("Jamie Foxx")
                    .orElseGet(() -> actorRepo.save(new Actor(null, "Jamie Foxx", new ArrayList<>())));

            // ----------------------
            // RELACIONES N:M
            // ----------------------
            // Inicializamos listas si son null para evitar NullPointerException
            if (matthew.getPeliculas() == null) matthew.setPeliculas(new ArrayList<>());
            if (hathaway.getPeliculas() == null) hathaway.setPeliculas(new ArrayList<>());
            if (foxx.getPeliculas() == null) foxx.setPeliculas(new ArrayList<>());
            if (interstellar.getActores() == null) interstellar.setActores(new ArrayList<>());
            if (soul.getActores() == null) soul.setActores(new ArrayList<>());

            matthew.addPelicula(interstellar);
            hathaway.addPelicula(interstellar);
            foxx.addPelicula(soul);

            // Guardar cambios (cascade debería encargarse, pero aseguramos persistencia)
            actorRepo.save(matthew);
            actorRepo.save(hathaway);
            actorRepo.save(foxx);

            peliculaRepo.save(interstellar);
            peliculaRepo.save(soul);

            System.out.println(">>> DATOS DE PRUEBA INSERTADOS CORRECTAMENTE");
        };
    }
}
