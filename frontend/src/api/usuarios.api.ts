import axios from 'axios';
import type {UsuarioCreateUpdateDTO, UsuarioDTO} from '../types/Usuario';

const API_URL = 'http://localhost:8081/api/usuarios';

export const getUsuarios = async (): Promise<UsuarioDTO[]> => {
    try {
        const response = await axios.get<UsuarioDTO[]>(API_URL);
        return response.data;
    } catch (error) {
        console.error("Error al obtener usuarios:", error);
        return [];
    }
};

export const crearUsuario = async (data: UsuarioCreateUpdateDTO): Promise<UsuarioDTO> => {
    const response = await axios.post<UsuarioDTO>(API_URL, data);
    return response.data;
};