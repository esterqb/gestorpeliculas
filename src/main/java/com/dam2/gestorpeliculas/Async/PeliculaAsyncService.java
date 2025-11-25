package com.dam2.gestorpeliculas.Async;

import com.dam2.gestorpeliculas.DTO.PeliculaDTO;
import com.dam2.gestorpeliculas.domain.Pelicula;
import com.dam2.gestorpeliculas.repository.PeliculaRepository;
import com.dam2.gestorpeliculas.service.PeliculaService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Stream;

@Service
public class PeliculaAsyncService {
    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private ApplicationContext context;


    // Tarea 1a
    public String tareaLenta(String titulo) {
        try {
            System.out.println("Iniciando tarea para " + titulo + " en " + Thread.currentThread().getName());
            Thread.sleep(3000); // simula proceso lento (3 segundos)
            System.out.println("Terminando tarea para " + titulo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Procesada " + titulo;
    }

    @Async("taskExecutor")
    // Tarea 1b
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

    // Tarea 2
    @Async("taskExecutor")
    // Retornamos un completableFuture de string
    public CompletableFuture<String> reproducir(String titulo) {
        long inicio = System.currentTimeMillis();
        try {
            System.out.println("Iniciando " + titulo + " en " + Thread.currentThread().getName());
            // Con esto reproducimos durante un periodo aleatorio 1-5 segundos
            int milisegundosAleatorios = (new Random().nextInt(5)+1) * 1000;
            Thread.sleep(milisegundosAleatorios);

            System.out.println("Terminando película " + titulo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // El tiempo es el actual - el inicial
        long tiempoTotalReproduccion = System.currentTimeMillis() - inicio;
        System.out.println("Procesada la película: " + titulo + " en " + tiempoTotalReproduccion + " milisegundos");

        // Retornamos la tarea cuando se completa
        return CompletableFuture.completedFuture("Procesada la película: " + titulo + " en " + tiempoTotalReproduccion + " milisegundos");
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
                lista.add(p);
            }

            this.peliculaRepository.saveAll(lista);

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

                lista.add(p);
            }

            this.peliculaRepository.saveAll(lista);

            System.out.println("Finalizado XML: " + fichero);

        } catch (Exception e) {
            System.err.println("Error en XML " + fichero + ": " + e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    // Ejercicio 3, llama a importarCsvAsync & importarCsvAsync
    public void importarCarpeta(String rutaCarpeta) throws IOException {
        long inicio = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        try (Stream<Path> paths = Files.list(Paths.get(rutaCarpeta))) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                String nombre = path.toString().toLowerCase();
                if (nombre.endsWith(".csv") || nombre.endsWith(".txt")) {
                    futures.add(context.getBean(PeliculaAsyncService.class).importarCsvAsync(path));
                } else if (nombre.endsWith(".xml")) {
                    futures.add(context.getBean(PeliculaAsyncService.class).importarXmlAsync(path));

                }
            });
        }
        // Esperar a que terminen todas las tareas asíncronas
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long fin = System.currentTimeMillis();
        System.out.println("Importación completa en " + (fin - inicio) + " ms");
    }

    // Ejercicio 4, llamamos a votarComoJurado()
    public HashMap<String, Integer> simularVotacionesAleatorias(int numeroVotaciones) {
        long inicio = System.currentTimeMillis();

        //de la bd
        List<PeliculaDTO> peliculasCandidatas = peliculaService.listar();
        ConcurrentHashMap<String, Integer> registroVotos = new ConcurrentHashMap<>();
        peliculasCandidatas.forEach(p -> registroVotos.put(p.getTitulo(), 0));

        Semaphore semaforo = new Semaphore(5);
        List<CompletableFuture<Void>> resultadosFuturos = new ArrayList<>();
        Random rnd = new Random();

        for (int i = 0; i < numeroVotaciones; i++) {
            //llama al jurado async
            resultadosFuturos.add(context.getBean(PeliculaAsyncService.class).votarComoJurado(
                    registroVotos,
                    peliculasCandidatas.get(rnd.nextInt(peliculasCandidatas.size())).getTitulo(),
                    semaforo,
                    i + 1));
        }

        // Esperar a que terminen todas las votaciones
        CompletableFuture.allOf(resultadosFuturos.toArray(new CompletableFuture[0])).join();

        long tiempoTotalVotacion = System.currentTimeMillis() - inicio;
        System.out.println("Votación realizada en: " + tiempoTotalVotacion + " ms");
        System.out.println("---- RECUENTO FINAL ----");

        // Mostrar resultados ordenados
        registroVotos.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() + " puntos"));

        return new HashMap<>(registroVotos);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> votarComoJurado(
            ConcurrentHashMap<String, Integer> votos,
            String titulo,
            Semaphore semaforo,
            int idJurado) {
        try {
            semaforo.acquire();
            int puntos = new Random().nextInt(11); //0-10 puntos
            votos.merge(titulo, puntos, Integer::sum);
            System.out.println("Jurado " + idJurado + " vota " + puntos + " puntos a " + titulo);

            Thread.sleep(200); //tiempo de votación
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaforo.release();
        }

        return CompletableFuture.completedFuture(null);
    }
}
