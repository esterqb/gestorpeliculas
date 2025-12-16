import React, { useEffect, useState } from 'react';
import type { PeliculaDTO } from '../types/Pelicula';
import { useUser } from '../context/UserContext';
import { getWatchlist } from '../services/watchlistService';
import MovieCard from '../components/MovieCard';

const containerStyle: React.CSSProperties = {
    backgroundColor: 'black',
    minHeight: '100vh',
    color: 'white',
};
const titleStyle: React.CSSProperties = {
    fontSize: '2.5rem',
    padding: '40px 40px 20px 40px',
};
const listGridStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: 'center',
    gap: '20px',
    padding: '0 40px 40px 40px',
};


const MyListPage: React.FC = () => {
    const { currentUser } = useUser();
    const currentUserId = currentUser?.id || 0;

    const [watchlist, setWatchlist] = useState<PeliculaDTO[]>([]);
    const [loading, setLoading] = useState(true);

    const loadWatchlist = () => {
        setWatchlist(getWatchlist(currentUserId));
        setLoading(false);
    };

    useEffect(() => {
        loadWatchlist();
    }, [currentUserId]);



    if (loading) {
        return <div style={containerStyle}>Cargando Mi Lista...</div>;
    }

    return (
        <div style={containerStyle}>
            <h1 style={titleStyle}>Mi Lista</h1>

            {watchlist.length === 0 ? (
                <p style={{ padding: '0 40px' }}>Tu lista está vacía. ¡Añade algunas películas desde el catálogo!</p>
            ) : (
                <div style={listGridStyle}>
                    {watchlist.map((pelicula) => (
                        <MovieCard
                            key={pelicula.id}
                            pelicula={pelicula}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};

export default MyListPage;