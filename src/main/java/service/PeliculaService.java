package service;


import domain.Director;
import domain.Pelicula;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import repository.DirectorRepository;
import repository.PeliculaRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


@Service
@Getter
@RequiredArgsConstructor
public class PeliculaService {
    private final List<Pelicula> peliculas = new ArrayList<>();
    private final PeliculaRepository peliculaRepository;
    private final DirectorRepository directorRepository;

/*
    public PeliculaService() {
        peliculas.add(new Pelicula(1L, "Interstellar", 169, LocalDate.of(2014, 11, 7),
                "Exploradores espaciales buscan un nuevo hogar para la humanidad.",6,null,null,null));
        peliculas.add(new Pelicula(2L, "The Dark Knight", 152, LocalDate.of(2008, 7, 18),
                "Batman enfrenta al Joker en una lucha por el alma de Gotham.",4,null,null,null));
        peliculas.add(new Pelicula(3L, "Soul", 100, LocalDate.of(2020, 12, 25),
                "Un músico descubre el sentido de la vida más allá de la muerte.",5,null,null,null));
    }
*/

    public List<Pelicula> mejores_peliculas(int valoracion){
        List<Pelicula> peliculas_aux= new ArrayList<>();
        for (Pelicula p : peliculas) {
            if (p.getValoracion()>=valoracion) {
                peliculas_aux.add(p);
            }
        }
        return peliculas_aux;
    }

    public List<Pelicula> listar() {
        return peliculas;
    }

    public Pelicula buscarPorId(Long id) {
        for (Pelicula p : peliculas) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
        /*
        * return peliculas.stream()                 // convierte la lista en un flujo de datos
        .filter(p -> p.getId().equals(id)) // se queda solo con las películas cuyo id coincide
        .findFirst()                       // toma la primera coincidencia (si existe)
        .orElse(null);                     // devuelve esa película o null si no hay
        * */
    }

    public void agregar(Pelicula pelicula) {
        peliculas.add(pelicula);
    }

    //EJERCICIO1_ EL TIEMPO NO SE MULTIPLICA
    public String tareaLenta(String titulo) {
        try {
            System.out.println("Iniciando tarea para " + titulo + " en " + Thread.currentThread().getName());
            Thread.sleep(3000); // TARDA 3 SEGUNDOS
            System.out.println("Terminando tarea para " + titulo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Procesada " + titulo;
    }

    @Async("taskExecutor")
    public CompletableFuture<String> tareaLenta2(String titulo) {
        try {
            System.out.println("Iniciando " + titulo + " en " + Thread.currentThread().getName());
            Thread.sleep(3000);
            System.out.println("Terminando " + titulo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Procesada " + titulo);
    }

    //EJERCICIO 2 - REPRODUCCIÓN DE PELÍCULAS
    @Async("taskExecutor")
    public CompletableFuture<String> reproducir(String titulo) {
        try {
            System.out.println("Reproduciendo " + titulo + " en " + Thread.currentThread().getName()); //Empieza
            int tiempoAleatorio = 1000 + new java.util.Random().nextInt(5000); //Tiempo aleatorio 1-5 segundos
            Thread.sleep(tiempoAleatorio);
            System.out.println("Terminando " + titulo+ ": " + tiempoAleatorio + "ms."); //Termina
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Procesada " + titulo);
    }

    public void importarCarpeta(String rutaCarpeta) throws IOException {
        long inicio = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        try (Stream<Path> paths = Files.list(Paths.get(rutaCarpeta))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                String nombre = path.toString().toLowerCase();
                if (nombre.endsWith(".csv") || nombre.endsWith(".txt")) {
                    futures.add(importarCsvAsync(path));
                } else if (nombre.endsWith(".xml")) {
                    futures.add(importarXmlAsync(path));
                }
            });
        }
        // Esperar a que terminen todas las tareas asíncronas
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long fin = System.currentTimeMillis();
        System.out.println("Importación completa en " + (fin - inicio) + " ms");
    }

    @PostConstruct //método para que funciona importarcarpeta con postconstruct para que se ejecute y me deje ver las peliculas en localhost
    public void init() throws IOException {
        importarCarpeta("C:/Users/ecc20/Documents/SpringBoot/src/main/resources/peliculas/");
    }


    @Async("taskExecutor")
    public CompletableFuture<Void> importarCsvAsync(Path fichero) {
        try {
            System.out.println("Procesando CSV: " + fichero + " en " + Thread.currentThread().getName());

            List<Pelicula> lista = new ArrayList<>();

            List<String> lineas = Files.readAllLines(fichero);
            lineas.remove(0); // suponemos encabezado

            for (String linea : lineas) {
                String[] campos = linea.split(";");
                Pelicula p = new Pelicula();
                p.setTitulo(campos[0]);
                p.setDuracion(Integer.parseInt(campos[1]));
                p.setFechaEstreno(LocalDate.parse(campos[2]));
                p.setSinopsis(campos[3]);


                String nombreDirector = campos[4]; // ajusta según tu CSV
                Director director = directorRepository.findByNombre(nombreDirector);
                if (director == null) {
                    director = new Director();
                    director.setNombre(nombreDirector);
                    directorRepository.save(director);
                }
                p.setDirector(director);


                lista.add(p);
            }

            peliculaRepository.saveAll(lista);
            peliculas.addAll(lista);


            System.out.println("Finalizado CSV: " + fichero);

        } catch (Exception e) {
            System.err.println("Error en CSV " + fichero + ": " + e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> importarXmlAsync(Path fichero) {
        try {
            System.out.println("Procesando XML: " + fichero + " en " + Thread.currentThread().getName());

            List<Pelicula> lista = new ArrayList<>();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(fichero.toFile());
            NodeList nodos = doc.getElementsByTagName("pelicula");

            for (int i = 0; i < nodos.getLength(); i++) {
                Element e = (Element) nodos.item(i);

                Pelicula p = new Pelicula();
                p.setTitulo(e.getElementsByTagName("titulo").item(0).getTextContent());
                p.setDuracion(Integer.parseInt(e.getElementsByTagName("duracion").item(0).getTextContent()));
                p.setFechaEstreno(LocalDate.parse(e.getElementsByTagName("fechaEstreno").item(0).getTextContent()));
                p.setSinopsis(e.getElementsByTagName("sinopsis").item(0).getTextContent());

                String nombreDirector = e.getElementsByTagName("director").item(0).getTextContent();
                Director director = directorRepository.findByNombre(nombreDirector);
                if (director == null) {
                    director = new Director();
                    director.setNombre(nombreDirector);
                    directorRepository.save(director);
                }
                p.setDirector(director);

                lista.add(p);
            }

            peliculaRepository.saveAll(lista);
            peliculas.addAll(lista);


            System.out.println("Finalizado XML: " + fichero);

        } catch (Exception e) {
            System.err.println("Error en XML " + fichero + ": " + e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    public String importarCarpetaMillis(String rutaCarpeta) throws IOException {
        long inicio = System.currentTimeMillis();
        List<CompletableFuture<Void>> futuroList = new ArrayList<>();
        rutaCarpeta="C:/Users/ecc20/Documents/SpringBoot/src/main/resources/peliculas/";

        try (Stream<Path> paths = Files.list(Paths.get(rutaCarpeta))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                String nombre = path.toString().toLowerCase();
                if (nombre.endsWith(".csv")) {
                    futuroList.add(importarCsvAsync(path));
                } else if (nombre.endsWith(".xml")) {
                    futuroList.add(importarXmlAsync(path));
                }
            });
        }
        CompletableFuture.allOf(futuroList.toArray(new CompletableFuture[0])).join();//Acaban de importarse todas
        long fin = System.currentTimeMillis();
        double segundos = (fin - inicio) / 1000.0;
        return "Importadas. " + segundos + " segundos. Número de películas cargadas: " + peliculas.size();
    }


    //EJ 4 Oscars
    public String votarOscar() {
        // 1 cada película es candidata
        List<Pelicula> listaPeliculas = listar();
        StringBuilder sb = new StringBuilder();
        sb.append("Candidatos a los Óscar:<br>");
        for (Pelicula p : listaPeliculas) {
            sb.append("🏆 ").append(p.getTitulo()).append("<br>");
        }
        sb.append("<br>");

        // 3
        Map<String, Integer> votos = new HashMap<>();
        StringBuilder resultado = new StringBuilder();
        Semaphore semaforo = new Semaphore(5); //5 hilos a la vez
        List<CompletableFuture<Void>> futuros = new ArrayList<>();
        int numJurados = 10;

        // 2.cada hilo representa a un jurado que vota
        for (int i = 1; i <= numJurados; i++) {
            for (Pelicula p : listaPeliculas) {
                futuros.add(juradoVotar(p.getTitulo(), votos, resultado, semaforo, i));
            }
        }

        //esperar a que terminen todas las votaciones
        CompletableFuture.allOf(futuros.toArray(new CompletableFuture[0])).join();

        //mostrar resultados
        resultado.append("<br><b>Resultados finales:</b><br>");
        for (Map.Entry<String, Integer> entry : votos.entrySet()) {
            resultado.append(entry.getKey()).append(": ").append(entry.getValue()).append(" puntos<br>");
        }

        //Devolver todo
        return sb.toString() + resultado.toString();
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> juradoVotar(String pelicula, Map<String,Integer> votos, StringBuilder resultado, Semaphore semaforo, int idJurado){ //devuelve que acaba cuando termine de ejecutar las tareas
        try{
            semaforo.acquire(); //acceder a la votacion
            int puntos= ThreadLocalRandom.current().nextInt(0,11);//dar de 0 a 10 puntos
            votos.merge(pelicula,puntos,Integer::sum);//sumar los puntos dados por el jurado
            String output="El jurado " + idJurado+" le da " + puntos+ " puntos a la película "+pelicula+"<br>";
            System.out.println(output);
            synchronized(resultado){
                resultado.append(output);
            }
            Thread.sleep(300);//tiempo para hacer la votación
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }finally{
            semaforo.release(); //salir de la votaciñon
        }
        return CompletableFuture.completedFuture(null);
    }

}
