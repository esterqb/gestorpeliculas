import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getPeliculaById } from '../api/peliculas.api';
import { getDirectorById } from '../api/directores.api';
import { getActoresByIds } from '../api/actores.api';
import { useUser } from '../context/UserContext';
import { getWatchlist, addPeliculaToWatchlist, removePeliculaFromWatchlist } from '../services/watchlistService';
import type { PeliculaDTO } from '../types/Pelicula';
import type { DirectorDTO } from '../types/Director';
import type { ActorDTO } from '../types/Actor';

const ADD_ICON_URL = 'https://cdn-icons-png.flaticon.com/512/561/561169.png'; // Signo de suma (+)
const ADDED_ICON_URL = 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Spring_Boot.svg/1200px-Spring_Boot.svg.png'; // Spring Boot
const REMOVE_ICON_URL = 'https://cdn-icons-png.flaticon.com/512/1214/1214594.png'; // Papelera (Eliminar)

const MovieDetails: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const movieId = id ? parseInt(id) : null;
    const { currentUser } = useUser();
    const currentUserId = currentUser?.id || 0;

    const [pelicula, setPelicula] = useState<PeliculaDTO | null>(null);
    const [director, setDirector] = useState<DirectorDTO | null>(null);
    const [actores, setActores] = useState<ActorDTO[]>([]);
    const [isInWatchlist, setIsInWatchlist] = useState(false);

    const [isHovering, setIsHovering] = useState(false);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // fetch
    useEffect(() => {
        if (!movieId) {
            setError("ID de película no válido.");
            setLoading(false);
            return;
        }

        const fetchDetails = async () => {
            setLoading(true);
            setError(null);

            try {
                const pData = await getPeliculaById(movieId);
                if (!pData) {
                    setError("Película no encontrada.");
                    setLoading(false);
                    return;
                }
                setPelicula(pData);

                const watchlist = getWatchlist(currentUserId);
                setIsInWatchlist(watchlist.some(p => p.id === pData.id));

                const directorPromise = getDirectorById(pData.directorId);
                const actoresPromise = getActoresByIds(pData.actoresIds);

                const [directorResult, actoresResult] = await Promise.all([directorPromise, actoresPromise]);

                setDirector(directorResult);
                setActores(actoresResult);

            } catch (err) {
                setError("Error al cargar los detalles de la película.");
            } finally {
                setLoading(false);
            }
        };

        // si el usuario cambia, que cambien sus pelis
        fetchDetails();
    }, [movieId, currentUserId]);

    // Estilo boton de añadir a mi lista
    const handleAddToList = () => {
        if (!pelicula) return;

        if (isInWatchlist) {
            removePeliculaFromWatchlist(pelicula.id, currentUserId);
            setIsInWatchlist(false);
            alert(`${pelicula.titulo} eliminado de Mi Lista.`);
        } else {
            addPeliculaToWatchlist(pelicula, currentUserId);
            setIsInWatchlist(true);
            alert(`${pelicula.titulo} añadido a Mi Lista.`);
        }
    };


    if (loading) {
        return <div style={{ paddingTop: '80px', padding: '40px', color: 'white' }}>Cargando detalles...</div>;
    }
    if (error) {
        return <div style={{ paddingTop: '80px', padding: '40px', color: 'red' }}>Error: {error}</div>;
    }
    if (!pelicula) {
        return <div style={{ paddingTop: '80px', padding: '40px', color: 'white' }}>Película no disponible.</div>;
    }

    const actoresPrincipales = actores.length > 0
        ? actores.map(a => a.nombre).join(', ')
        : "N/A";

    const directorNombre = director ? director.nombre : "Desconocido";

    // boton de añadir a mi lista etc
    let currentIconUrl = ADD_ICON_URL;
    let buttonTitle = "Añadir a Mi Lista";
    let iconFilter = 'invert(0)';

    if (isInWatchlist) {
        if (isHovering) {
            currentIconUrl = REMOVE_ICON_URL;
            buttonTitle = "Eliminar de Mi Lista";
            iconFilter = 'none';
        } else {
            currentIconUrl = ADDED_ICON_URL;
            buttonTitle = "Ya está en Mi Lista (Clic para eliminar)";
            iconFilter = 'none';
        }
    }

    return (
        <div style={{ backgroundColor: '#000', minHeight: '100vh', color: 'white', padding: '80px 40px 40px 40px' }}>

            {/* 1. TÍTULO Y BOTÓN DE LISTA (EN UNA SOLA FILA) */}
            <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                <h1 style={{ color: '#E50914', marginRight: '20px', margin: 0 }}>{pelicula.titulo}</h1>

                {/* BOTÓN DINÁMICO */}
                <button
                    onClick={handleAddToList}
                    onMouseEnter={() => setIsHovering(true)}
                    onMouseLeave={() => setIsHovering(false)}
                    style={{
                        backgroundColor: '#ffffff',
                        border: 'none',
                        borderRadius: '50%',
                        width: '40px',
                        height: '40px',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        cursor: 'pointer',
                        fontSize: '1.5rem',
                        transition: 'background-color 0.3s',
                        boxShadow: '0 2px 5px rgba(255, 255, 255, 0.2)',
                        padding: '5px',
                        marginLeft: '10px',
                        marginTop: '5px',
                    }}
                    title={buttonTitle}
                >
                    {/* ICONO DINÁMICO */}
                    <img
                        src={currentIconUrl}
                        alt={buttonTitle}
                        style={{
                            width: '100%',
                            height: '100%',
                            objectFit: 'contain',
                            filter: iconFilter
                        }}
                    />
                </button>
            </div>


            {/* 2. SECCIÓN DE METADATOS PRINCIPALES (Rating, Duración, Estreno) */}
            <p style={{ fontSize: '1.2rem', opacity: 0.8, marginBottom: '30px' }}>
                ⭐ **{pelicula.valoracion}/10** | {pelicula.duracion} min | Estreno: **{pelicula.fechaEstreno}**
            </p>

            {/* 3. CONTENEDOR FLEX PARA LAS DOS COLUMNAS (resto sin cambios) */}
            <div style={{ display: 'flex', gap: '40px', alignItems: 'flex-start' }}>

                {/* COLUMNA IZQUIERDA: PORTADA */}
                <div style={{ flexShrink: 0, width: '300px', borderRadius: '8px', overflow: 'hidden', boxShadow: '0 4px 15px rgba(0, 0, 0, 0.5)' }}>
                    {pelicula.posterUrl ? (
                        <img
                            src={pelicula.posterUrl}
                            alt={`Portada de ${pelicula.titulo}`}
                            style={{ width: '100%', height: 'auto', display: 'block' }}
                        />
                    ) : (
                        <div style={{ padding: '20px', backgroundColor: '#333', height: '450px', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                            No hay imagen
                        </div>
                    )}
                </div>

                {/* COLUMNA DERECHA: SINÓPSIS Y DATOS ADICIONALES */}
                <div style={{ flexGrow: 1, paddingTop: '10px' }}>

                    {/* SINÓPSIS */}
                    <div style={{ margin: '0 0 30px 0', borderLeft: '3px solid #E50914', paddingLeft: '15px' }}>
                        <h2 style={{ fontSize: '1.8rem', marginBottom: '10px' }}>Sinopsis</h2>
                        <p style={{ fontSize: '1rem', lineHeight: '1.6', opacity: 0.9 }}>
                            {pelicula.sinopsis}
                        </p>
                    </div>

                    {/* INFORMACIÓN ADICIONAL (Director, Actores) */}
                    <div>
                        <h3>Información del Equipo</h3>
                        <p style={{ marginTop: '10px' }}>
                            **Director:** <span style={{ color: '#aaa' }}>{directorNombre}</span>
                        </p>
                        <p>
                            **Reparto Principal:** <span style={{ color: '#aaa' }}>{actoresPrincipales}</span>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MovieDetails;