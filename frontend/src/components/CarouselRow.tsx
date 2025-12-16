import React, { useRef } from 'react';
import type { PeliculaDTO } from '../types/Pelicula';
import MovieCard from './MovieCard';

interface CarouselRowProps {
    title: string;
    peliculas: PeliculaDTO[];
}

const SCROLL_DISTANCE = 400;

// flechas en vez de scroll en las secciones de pelis
const ICONO_IZQUIERDA_URL = 'https://cdn-icons-png.flaticon.com/512/3236/3236910.png';
const ICONO_DERECHA_URL = 'https://cdn-icons-png.flaticon.com/512/3236/3236907.png';

const CarouselRow: React.FC<CarouselRowProps> = ({ title, peliculas }) => {
    const listRef = useRef<HTMLDivElement>(null);

    const scroll = (direction: 'left' | 'right') => {
        if (listRef.current) {
            const scrollAmount = direction === 'left' ? -SCROLL_DISTANCE : SCROLL_DISTANCE;
            listRef.current.scrollBy({ left: scrollAmount, behavior: 'smooth' });
        }
    };

    const rowContainerStyle: React.CSSProperties = {
        marginBottom: '40px',
        position: 'relative',
    };

    const rowTitleStyle: React.CSSProperties = {
        fontSize: '1.4rem',
        fontWeight: 'bold',
        marginBottom: '10px',
        marginTop: '20px',
        padding: '0 40px',
    };

    const cardListStyle: React.CSSProperties = {
        display: 'flex',
        overflowX: 'hidden',
        padding: '40px 50px 40px 50px',
        position: 'relative',
    };

    const rowWrapperStyle: React.CSSProperties = {
        position: 'relative',
    };

    const buttonStyle: React.CSSProperties = {
        position: 'absolute',
        top: '50%',
        transform: 'translateY(-50%)',
        zIndex: 20,
        backgroundColor: '#000000',
        border: 'none',
        height: '40px',
        width: '40px',
        borderRadius: '50%',
        cursor: 'pointer',
        opacity: 0,
        transition: 'opacity 0.3s',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        margin: '0 10px',
        padding: 0,
    };

    const iconImageStyle: React.CSSProperties = {
        width: '70%',
        height: '70%',
        objectFit: 'contain',
        filter: 'invert(1)',
    };


    return (
        <div style={rowContainerStyle}>
            <h2 style={rowTitleStyle}>{title}</h2>

            <div className="row-wrapper" style={rowWrapperStyle}>

                {/*boton de izq*/}
                <button
                    className="nav-button"
                    style={{ ...buttonStyle, left: '10px' }}
                    onClick={() => scroll('left')}
                >
                    <img
                        src={ICONO_IZQUIERDA_URL}
                        alt="Anterior"
                        style={iconImageStyle}
                    />
                </button>

                {/*las movie cards*/}
                <div ref={listRef} style={cardListStyle}>
                    {peliculas.map((p) => (
                        p.posterUrl && <MovieCard key={p.id} pelicula={p} />
                    ))}
                </div>

                {/*button dcha*/}
                <button
                    className="nav-button"
                    style={{ ...buttonStyle, right: '10px' }}
                    onClick={() => scroll('right')}
                >
                    <img
                        src={ICONO_DERECHA_URL}
                        alt="Siguiente"
                        style={iconImageStyle}
                    />
                </button>
            </div>
        </div>
    );
};

export default CarouselRow;