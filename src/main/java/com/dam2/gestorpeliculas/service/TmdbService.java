package com.dam2.gestorpeliculas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.baseurl}")
    private String baseUrl;

    @Value("${tmdb.image.baseurl}")
    private String imageBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Simula la búsqueda de un póster por título.
     * En un entorno real, esto devolvería JSON y extraerías la ruta.
     */
    public String buscarPosterUrl(String tituloPelicula) {
        try {
            // Codificar el título para la URL
            String encodedTitle = tituloPelicula.replace(" ", "+");

            // Construir la URL de búsqueda (usando la API Key)
            String url = String.format("%s/search/movie?api_key=%s&query=%s",
                    baseUrl, apiKey, encodedTitle);

            // Ejecutar la búsqueda (solo obtenemos la respuesta JSON como String para simplificar)
            String response = restTemplate.getForObject(url, String.class);

            // ***** LÓGICA CLAVE *****
            // Aquí deberías parsear la respuesta JSON (usando Jackson o Gson),
            // encontrar el primer resultado y extraer la "poster_path" (ej: "/abX23c.jpg").
            String posterPath = extractPosterPath(response); // <-- Asumimos que esta función existe

            if (posterPath != null) {
                // Devolver la URL completa
                return imageBaseUrl + posterPath;
            }

        } catch (Exception e) {
            System.err.println("Error al conectar con TMDB: " + e.getMessage());
        }
        return null; // Si falla, devuelve null
    }

    // Nota: La implementación real de extractPosterPath requeriría manejar JSON.
    private String extractPosterPath(String jsonResponse) {
        // En un proyecto real: implementar la lógica de parsing JSON para obtener el poster_path.
        // Por ejemplo, si buscas "RoboCop", el path es: "/s3T7gHqOaO12r.jpg"

        // **Por ahora, puedes devolver una ruta fija para probar la conexión:**
        if (jsonResponse.contains("RoboCop")) {
            return "/m8FwOaY5eN6tH6Xm.jpg"; // Ejemplo de ruta de póster real
        }
        return null;
    }
}