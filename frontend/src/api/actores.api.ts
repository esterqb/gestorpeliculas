import axios from 'axios';
import type { ActorDTO } from '../types/Actor';

const API_URL = 'http://localhost:8081/api/actores';

// Función para obtener un solo actor
export const getActorById = async (id: number): Promise<ActorDTO | null> => {
    try {
        const response = await axios.get<ActorDTO>(`${API_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error al obtener actor con ID ${id}:`, error);
        return null;
    }
};

// Función para obtener varios actores por lista de IDs
export const getActoresByIds = async (ids: number[]): Promise<ActorDTO[]> => {
    const promises = ids.map(id => getActorById(id));
    const results = await Promise.all(promises);

    return results.filter((actor): actor is ActorDTO => actor !== null);
};