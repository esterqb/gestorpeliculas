import React from 'react';
import { Link } from 'react-router-dom';
import type { PeliculaDTO } from '../types/Pelicula';

interface MovieCardProps {
    pelicula: PeliculaDTO;
}

const CARD_WIDTH = '280px';
const IMAGE_HEIGHT = '420px';
const TITLE_HEIGHT = '30px';

const cardBaseStyle: React.CSSProperties = {
    minWidth: CARD_WIDTH,
    height: `calc(${IMAGE_HEIGHT} + ${TITLE_HEIGHT})`,
    marginRight: '15px',
    borderRadius: '4px',
    overflow: 'visible',
    position: 'relative',
    backgroundColor: 'transparent',
    transition: 'transform 0.3s ease-in-out, z-index 0.3s',
    boxSizing: 'content-box',
};

const imageContainerStyle: React.CSSProperties = {
    width: '100%',
    height: IMAGE_HEIGHT,
    borderRadius: '4px',
    overflow: 'hidden',
    boxShadow: '0 2px 5px rgba(0, 0, 0, 0.5)',
    position: 'relative',
    backgroundColor: '#333',
};

const imageStyle: React.CSSProperties = {
    width: '100%',
    height: '100%',
    objectFit: 'cover',
};



const MovieCard: React.FC<MovieCardProps> = ({ pelicula }) => {
    const [isHovered, setIsHovered] = React.useState(false);

    const currentCardStyle: React.CSSProperties = {
        ...cardBaseStyle,
        transform: isHovered ? 'scale(1.15)' : 'scale(1)',
        zIndex: isHovered ? 10 : 1,
    };

    const imageSource = pelicula.posterUrl || '';


    return (
        <Link
            to={`/peliculas/${pelicula.id}`}
            className="movie-card"
            style={{
                ...currentCardStyle,
                textDecoration: 'none',
                color: 'inherit',
                display: 'block',
            }}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            {/* 1. CONTENEDOR DE LA IMAGEN*/}
            <div style={imageContainerStyle}>
                {imageSource ? (
                    <img
                        src={imageSource}
                        alt={pelicula.titulo}
                        style={imageStyle}
                    />
                ) : (
                    // Fallback si no hay imagen
                    <div style={{ padding: '5px', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', textAlign: 'center' }}>
                        <span style={{ fontSize: '1.2em', fontWeight: 'bold' }}>{pelicula.titulo}</span>
                    </div>
                )}
            </div>

            {/* 2. TÍTULO DEBAJO DE LA IMAGEN */}
            <div style={{
                height: TITLE_HEIGHT,
                paddingTop: '5px',
                textAlign: 'left',
                overflow: 'hidden',
            }}>
                <span style={{
                    fontSize: '1rem',
                    fontWeight: 'normal',
                    color: 'white',
                }}>
                    {pelicula.titulo}
                </span>
            </div>

            {/* */}
        </Link>
    );
};

export default MovieCard;