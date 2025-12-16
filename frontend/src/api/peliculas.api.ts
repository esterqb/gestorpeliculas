import axios from 'axios';
import type { PeliculaDTO } from '../types/Pelicula';
import type { Categoria } from '../types/Categoria';

const API_URL_BASE = 'http://localhost:8081/api';

// Función para obtener todas las películas
export const getPeliculas = async (): Promise<PeliculaDTO[]> => {
    try {
        //se debe recibir PeliculaDTO
        const response = await axios.get<PeliculaDTO[]>(`${API_URL_BASE}/peliculas`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            console.error("Error de red o CORS al obtener películas:", error.message);
        } else {
            console.error("Error desconocido al obtener películas:", error);
        }
        return [];
    }
};

// Función para obtener una película por ID
export const getPeliculaById = async (id: number): Promise<PeliculaDTO | null> => {
    try {
        //Llamar al endpoint /api/peliculas/{id}
        const response = await axios.get<PeliculaDTO>(`${API_URL_BASE}/peliculas/${id}`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            console.error(`Error ${error.response?.status} al obtener película con ID ${id}:`, error.message);
        } else {
            console.error("Error desconocido:", error);
        }
        return null;
    }
};

// Función getCategorias
export const getCategorias = async (): Promise<Categoria[]> => {
    try {
        //Llama al endpoint /api/categorias
        const response = await axios.get<Categoria[]>(`${API_URL_BASE}/categorias`);
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error)) {
            console.error("Error de red o CORS al obtener categorías:", error.message);
        } else {
            console.error("Error desconocido al obtener categorías:", error);
        }
        return [];
    }
};