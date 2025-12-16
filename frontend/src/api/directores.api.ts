import axios from 'axios';
import type { DirectorDTO } from '../types/Director';

const API_URL = 'http://localhost:8081/api/directores';

export const getDirectorById = async (id: number): Promise<DirectorDTO | null> => {
    try {
        const response = await axios.get<DirectorDTO>(`${API_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error al obtener director con ID ${id}:`, error);
        return null;
    }
};