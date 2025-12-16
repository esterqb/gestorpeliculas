import React, { useEffect, useState, useMemo } from 'react';
import { getPeliculas, getCategorias } from '../api/peliculas.api';
import { getWatchlist } from '../services/watchlistService';
import { useUser } from '../context/UserContext';

import type { PeliculaDTO } from '../types/Pelicula';
import type { Categoria } from '../types/Categoria';
import HeroBanner from '../components/HeroBanner';
import CarouselRow from '../components/CarouselRow';

interface HomePageProps {
    showBanner?: boolean;
    viewType?: 'home' | 'catalog';
}

const rowTitleStyle: React.CSSProperties = {
    fontSize: '1.4rem',
    fontWeight: 'bold',
    marginBottom: '10px',
    marginTop: '20px',
    padding: '0 40px',
};


const HomePage: React.FC<HomePageProps> = ({ showBanner = true, viewType = 'home' }) => {

    const { currentUser } = useUser();
    const currentUserId = currentUser?.id || 0;
    const isChildProfile = currentUser?.rol === 'Infantil';

    const [peliculasData, setPeliculasData] = useState<PeliculaDTO[]>([]);
    const [categoriasData, setCategoriasData] = useState<Categoria[]>([]);
    const [watchlist, setWatchlist] = useState<PeliculaDTO[]>([]);
    const [loading, setLoading] = useState(true);

    const isCatalogView = viewType === 'catalog';

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const [peliculasResult, categoriasResult] = await Promise.all([getPeliculas(), getCategorias()]);

                const watchlistResult = getWatchlist(currentUserId);

                setPeliculasData(peliculasResult);
                setCategoriasData(categoriasResult);
                setWatchlist(watchlistResult);

            } catch (error) {
                console.error("Error al cargar datos iniciales:", error);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [currentUserId]);

    const [peliculas, categorias] = useMemo(() => {
        if (!isChildProfile) {
            return [peliculasData, categoriasData];
        }

        const infantilCategory = categoriasData.find(c => c.nombre === 'Infantil');
        const infantilCategoryId = infantilCategory?.id;

        if (!infantilCategoryId) {
            return [[], []];
        }

        const filteredPeliculas = peliculasData.filter(p => p.categoriasIds?.includes(infantilCategoryId));

        if (isCatalogView) {
            const categoriasAFiltrar = categoriasData.filter(c => filteredPeliculas.some(p => p.categoriasIds?.includes(c.id)));
            return [filteredPeliculas, categoriasAFiltrar];
        } else {
            return [filteredPeliculas, []];
        }
    }, [peliculasData, categoriasData, isChildProfile, isCatalogView]);


    const watchlistIds = watchlist.map(p => p.id);
    const peliculasSugeridas = peliculas.filter(p => !watchlistIds.includes(p.id));


    let bannerPeliculas = peliculas.filter(p => p.bannerUrl);

    // FALLBACK: Si no hay películas con bannerUrl, usa las primeras 5 con posterUrl.
    if (bannerPeliculas.length === 0 && peliculas.length > 0) {
        bannerPeliculas = peliculas
            .filter(p => p.posterUrl)
            .sort(() => 0.5 - Math.random())
            .slice(0, 5);
    }

    if (loading) {
        return <div style={{ paddingTop: '68px', padding: '40px', backgroundColor: 'black', color: 'white' }}>Cargando catálogo...</div>;
    }

    // Perfil infantil: solo se muestran de categoría (género) infantil
    if (isChildProfile && !isCatalogView) {
        const childWatchlist = watchlist.filter(p => peliculas.some(fp => fp.id === p.id));

        return (
            <div style={{ backgroundColor: 'black', minHeight: '100vh', color: 'white' }}>

                {/* */}
                {showBanner && <HeroBanner peliculas={bannerPeliculas} />}

                {/* 1. MI LISTA (solo si hay contenido) */}
                {childWatchlist.length > 0 && (
                    <>
                        <h2 style={rowTitleStyle}>Mi Lista</h2>
                        <CarouselRow
                            title=""
                            peliculas={childWatchlist}
                        />
                    </>
                )}

                {/* 2. SUGERENCIAS (Todas las peliculas infantiles que no ha guardado) */}
                {peliculasSugeridas.length > 0 && (
                    <>
                        <h2 style={rowTitleStyle}>Sugerencias</h2>
                        <CarouselRow
                            title=""
                            peliculas={peliculasSugeridas}
                        />
                    </>
                )}
                {peliculasSugeridas.length === 0 && childWatchlist.length === 0 && (
                    <p style={{ padding: '0 40px' }}>No hay películas infantiles disponibles o ya has guardado todas.</p>
                )}
            </div>
        );
    }

    // Perfil no infantil:
    const peliculasMejorValoradas = peliculas.filter(p => (p.valoracion ?? 0) >= 9);

    return (
        <div style={{ backgroundColor: 'black', minHeight: '100vh', color: 'white' }}>

            {/**/}
            {showBanner && <HeroBanner peliculas={bannerPeliculas} />}

            {/* SECCIONES PARA PERFIL ADULTO EN /inicio */}
            {!isCatalogView && (
                <>
                    {/* 1. SUGERENCIAS */}
                    {peliculasSugeridas.length > 0 && (
                        <>
                            <h2 style={rowTitleStyle}>Sugerencias para ti</h2>
                            {/* */}
                            <CarouselRow
                                title=""
                                peliculas={peliculasSugeridas.sort(() => 0.5 - Math.random()).slice(0, 4)}
                            />
                        </>
                    )}

                    {/* 2. MI LISTA */}
                    {watchlist.length > 0 && (
                        <>
                            <h2 style={rowTitleStyle}>Mi Lista</h2>
                            <CarouselRow
                                title=""
                                peliculas={watchlist}
                            />
                        </>
                    )}

                    {/* 3. MEJOR VALORADAS */}
                    {peliculasMejorValoradas.length > 0 && (
                        <>
                            <h2 style={rowTitleStyle}>Mejor valoradas</h2>
                            <CarouselRow
                                title=""
                                peliculas={peliculasMejorValoradas}
                            />
                        </>
                    )}
                </>
            )}

            {/**/}
            {categorias.map(categoria => {
                const peliculasDeCategoria = peliculas.filter(p => p.categoriasIds?.includes(categoria.id));

                if (peliculasDeCategoria.length === 0) {
                    return null;
                }

                return (
                    <CarouselRow
                        key={categoria.id}
                        title={categoria.nombre}
                        peliculas={peliculasDeCategoria}
                    />
                );
            })}

            {peliculas.length === 0 && <p style={{ padding: '0 40px' }}>No se encontraron películas. Verifica el servicio de Spring Boot.</p>}
        </div>
    );
};

export default HomePage;