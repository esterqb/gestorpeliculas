import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUsuarios } from '../api/usuarios.api';
import type { UsuarioDTO } from '../types/Usuario';
import ProfileCard from '../components/ProfileCard';
import { useUser } from '../context/UserContext';

interface ProfileData {
    id: number;
    username: string;
    imagenUrl?: string;
    esEspecial?: boolean;
    rol?: string;
}

const containerStyle: React.CSSProperties = {
    backgroundColor: 'black',
    minHeight: '100vh',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    color: 'white',
};
const titleStyle: React.CSSProperties = {
    fontSize: '3rem',
    marginBottom: '50px',
};
const listContainerStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    justifyContent: 'center',
};


//CONSTANTES DE IMAGEN (sin cambios)
const DEFAULT_AVATAR_URL = '/netflix.png';
const INFANTIL_AVATAR_URL = '/kids.png';
const AVATAR_MAP_KEY = 'springflixAvatars';

// FUNCIÓN AUXILIAR PARA LEER EL MAPA PERMANENTE (sin cambios)
const getStoredAvatars = (): Record<number, string> => {
    try {
        const data = localStorage.getItem(AVATAR_MAP_KEY);
        return data ? JSON.parse(data) : {};
    } catch (e) {
        return {};
    }
};

const ProfileSelectionPage: React.FC = () => {
    const navigate = useNavigate();
    const { login, currentUser } = useUser();
    const [usuarios, setUsuarios] = useState<UsuarioDTO[]>([]);
    const [loading, setLoading] = useState(true);
    const [persistedAvatars, setPersistedAvatars] = useState<Record<number, string>>({});

    // Datos Fijos para perfiles especiales (Infantil y Añadir)
    const perfilInfantil = {
        id: -1,
        username: "Infantil",
        imagenUrl: INFANTIL_AVATAR_URL,
        rol: "Infantil",
        email: "infantil@springflix.com",
    };
    const perfilAdd = {
        id: -2,
        username: "Añadir perfil",
        esEspecial: true,
    };


    useEffect(() => {
        const fetchUsuarios = async () => {
            const data = await getUsuarios();
            setUsuarios(data);
            setPersistedAvatars(getStoredAvatars());
            setLoading(false);
        };

        fetchUsuarios();

    }, [currentUser, navigate]);

    // Iniciar sesion
    const handleProfileClick = (id: number) => {
        if (id === perfilAdd.id) {
            alert("Aquí se abriría el formulario para crear un nuevo usuario.");
        } else {
            const selectedUser = usuarios.find(u => u.id === id);

            if (selectedUser) {
                const customAvatarUrl = persistedAvatars[selectedUser.id];
                const initialAvatarUrl = customAvatarUrl || DEFAULT_AVATAR_URL;

                login(selectedUser, initialAvatarUrl);
                console.log(`Usuario ${selectedUser.username} seleccionado. Iniciando sesión.`);

                navigate('/inicio');

            } else if (id === perfilInfantil.id) {
                //Infantil
                login(perfilInfantil as UsuarioDTO, INFANTIL_AVATAR_URL);
                console.log(`Perfil Infantil seleccionado. Iniciando sesión.`);

                navigate('/inicio');
            }
        }
    };

    if (loading) {
        return <div style={containerStyle}>Cargando perfiles...</div>;
    }

    // Lista de perfiles para renderizar:
    const profiles: ProfileData[] = [
        ...usuarios.map((u) => {
            const customAvatarUrl = persistedAvatars[u.id];

            return {
                id: u.id,
                username: u.username || 'Usuario Desconocido',
                imagenUrl: customAvatarUrl || DEFAULT_AVATAR_URL,
                rol: u.rol
            };
        }),
        perfilInfantil as ProfileData,
        perfilAdd as ProfileData,
    ];


    // @ts-ignore
    return (
        <div style={containerStyle}>
            <h1 style={titleStyle}>¿Quién eres? Elige tu perfil</h1>

            <div style={listContainerStyle}>
                {profiles.map((p) => (
                    <ProfileCard
                        key={p.id}
                        nombre={p.username}
                        imagenUrl={p.imagenUrl}
                        esEspecial={p.esEspecial}
                        onClick={() => handleProfileClick(p.id)}
                    />
                ))}
            </div>
        </div>
    );
};

export default ProfileSelectionPage;