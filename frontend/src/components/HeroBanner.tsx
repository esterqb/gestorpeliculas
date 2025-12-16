import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { IoIosPlay } from 'react-icons/io';
import type { PeliculaDTO } from '../types/Pelicula';

const BANNER_DURATION_MS = 15000;

interface HeroBannerProps {
    peliculas: PeliculaDTO[];
}

const HeroBanner: React.FC<HeroBannerProps> = ({ peliculas: peliculasProp }) => {
    const navigate = useNavigate();

    const [peliculas, setPeliculas] = useState<PeliculaDTO[]>(
        peliculasProp.filter(p => p.bannerUrl || p.posterUrl)
    );

    const [currentIndex, setCurrentIndex] = useState(0);

    const peliculaActiva = peliculas[currentIndex];

    // EFECTO 1: Sincronización de Datos
    useEffect(() => {
        if (peliculasProp && peliculasProp.length > 0) {
            const validPeliculas = peliculasProp.filter(p => p.bannerUrl || p.posterUrl);

            setPeliculas(validPeliculas);
            setCurrentIndex(0); // Reinicia el carrusel
        } else {
            setPeliculas([]);
        }
    }, [peliculasProp]);

    // EFECTO 2: Timer y Rotación banner
    useEffect(() => {
        if (peliculas.length === 0) return;

        const intervalId = setInterval(() => {
            setCurrentIndex((prevIndex) =>
                (prevIndex + 1) % peliculas.length
            );
        }, BANNER_DURATION_MS);

        return () => clearInterval(intervalId);
    }, [peliculas]);


    let imageUrl = '';

    if (peliculaActiva?.bannerUrl) {
        imageUrl = peliculaActiva.bannerUrl;
    } else if (peliculaActiva?.posterUrl) {
        imageUrl = peliculaActiva.posterUrl;
    }

    const backgroundStyle: React.CSSProperties = {
        backgroundImage: `linear-gradient(to top, rgba(0,0,0,1) 0%, rgba(0,0,0,0.5) 50%, rgba(0,0,0,0.1) 100%), url(${imageUrl})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        height: '80vh',
        display: 'flex',
        alignItems: 'center',
        color: 'white',
        padding: '0 40px',
        transition: 'background-image 1s ease-in-out',
        backgroundColor: 'black'
    };
    // ----------------------------------------------------

    const handleVerDatos = () => {
        if (peliculaActiva) {
            navigate(`/peliculas/${peliculaActiva.id}`);
        }
    };

    if (!peliculaActiva) {
        return <div style={{ height: '80vh', backgroundColor: 'black' }}></div>;
    }

    // Usamos sinopsis, pero no la forzamos a existir en el filtro
    const sinopsisCorta = peliculaActiva.sinopsis
        ? peliculaActiva.sinopsis.substring(0, 180) + '...'
        : 'Sinopsis no disponible.';


    return (
        <div style={backgroundStyle}>
            <div style={{ maxWidth: '600px' }}>
                {/* TÍTULO */}
                <h1 style={{ fontSize: '3.5rem', marginBottom: '1rem' }}>
                    {peliculaActiva.titulo}
                </h1>

                {/* SINOPSIS */}
                <p style={{ fontSize: '1.2rem', marginBottom: '2rem', lineHeight: '1.4' }}>
                    {sinopsisCorta}
                </p>

                {/* BOTÓN */}
                <button
                    onClick={handleVerDatos}
                    style={{
                        padding: '10px 20px',
                        fontSize: '1.2rem',
                        fontWeight: 'bold',
                        backgroundColor: 'white',
                        color: 'black',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        display: 'flex',
                        alignItems: 'center',
                    }}
                >
                    <IoIosPlay style={{ marginRight: '8px' }} />
                    Ver datos
                </button>
            </div>
        </div>
    );
};

export default HeroBanner;