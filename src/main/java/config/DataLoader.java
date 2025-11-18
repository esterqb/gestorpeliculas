package config;

import domain.Actor;
import domain.Director;
import domain.FichaTecnica;
import domain.Pelicula;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.ActorRepository;
import repository.DirectorRepository;
import repository.FichaTecnicaRepository;
import repository.PeliculaRepository;

import java.time.LocalDate;
import java.util.ArrayList;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(ActorRepository actorRepository,
                               DirectorRepository directorRepository,
                               FichaTecnicaRepository fichaTecnicaRepository,
                               PeliculaRepository peliculaRepository) {
        return args -> {
            if (directorRepository.count() > 0) {
                System.out.println("Los datos ya figuran, no se activa Data Loader.");
                return;
            }
            System.out.println("Cargando datos: Data Loader...");

            Director nolan = new Director(null, "Christopher Nolan", new ArrayList<>());
            Director docter = new Director(null, "Peter Docter", new ArrayList<>());
            directorRepository.save(nolan);
            directorRepository.save(docter);

            FichaTecnica f1 = new FichaTecnica(null, "Christopher Nolan","Exploradores espaciales viajan a través de un agujero de gusano...", 169, "EEUU");
            FichaTecnica f2 = new FichaTecnica(null, "Peter Docter","Un músico descubre el verdadero sentido de la vida...", 100, "EEUU");
            fichaTecnicaRepository.save(f1);
            fichaTecnicaRepository.save(f2);

            Pelicula interstellar = new Pelicula(null,"Interstellar", 169, LocalDate.of(2014,11,7), "Exploradores espaciales viajan a través de un agujero de gusano...",8,f1,nolan,new ArrayList<>());
            Pelicula soul = new Pelicula(null, "Soul", 100, LocalDate.of(2020,12,25),"Un músico descubre el verdadero sentido de la vida...", 8, f2, docter, new ArrayList<>());
            peliculaRepository.save(interstellar);
            peliculaRepository.save(soul);

            Actor matthew=new Actor(null, "Matthew McConaughey",new ArrayList<>());
            Actor hathaway=new Actor(null, "Anne Hathaway",new ArrayList<>());
            Actor foxx =new Actor(null,"Jamie Foxx", new ArrayList<>());
            //Añadir a peliculas (array de la clase actor) - Many to many
            matthew.addPelicula(interstellar);
            hathaway.addPelicula(interstellar);
            foxx.addPelicula(soul);
            interstellar.getActors().add(matthew);
            interstellar.getActors().add(hathaway);
            soul.getActors().add(foxx);

            actorRepository.save(matthew);
            actorRepository.save(hathaway);
            actorRepository.save(foxx);
            System.out.println("Datos cargados.");
        };
    }
}
