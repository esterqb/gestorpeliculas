import type { PeliculaDTO } from '../types/Pelicula';

// Genera una clave única por usuario
const getWatchlistKey = (userId: number): string => {
    if (userId < 1) { //Previene problemas con IDs especiales como -1 (Infantil) o -2 (Añadir)
        return 'myListPeliculas_shared';
    }
    return `myListPeliculas_${userId}`;
};


/**
 * Obtiene la lista actual de películas desde localStorage para un usuario específico.
 */
export const getWatchlist = (userId: number): PeliculaDTO[] => {
    const key = getWatchlistKey(userId);
    const json = localStorage.getItem(key);
    return json ? JSON.parse(json) : [];
};

/**
 * Añade una película a la lista si no está ya, y la guarda en localStorage.
 */
export const addPeliculaToWatchlist = (pelicula: PeliculaDTO, userId: number): PeliculaDTO[] => {
    const key = getWatchlistKey(userId);
    const currentList = getWatchlist(userId);

    // Evitar duplicados
    if (currentList.some(p => p.id === pelicula.id)) {
        console.warn(`${pelicula.titulo} ya está en Mi Lista.`);
        return currentList;
    }

    const newList = [...currentList, pelicula];
    localStorage.setItem(key, JSON.stringify(newList));
    return newList;
};

/**
 * Elimina una película de la lista por ID.
 */
export const removePeliculaFromWatchlist = (peliculaId: number, userId: number): PeliculaDTO[] => {
    const key = getWatchlistKey(userId);
    const currentList = getWatchlist(userId);

    const newList = currentList.filter(p => p.id !== peliculaId);
    localStorage.setItem(key, JSON.stringify(newList));
    return newList;
};