import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { IoIosSearch, IoIosNotifications, IoIosArrowDown } from 'react-icons/io';
import { useUser } from '../context/UserContext';
import AvatarModal from './AvatarModal';

const headerStyle: React.CSSProperties = {
    backgroundColor: 'rgba(0, 0, 0, 0.8)',
    height: '68px',
    display: 'flex',
    alignItems: 'center',
    padding: '0 40px',
    position: 'fixed',
    top: 0,
    left:'0',
    width: '100%',
    zIndex: 10,
    boxSizing: 'border-box',
    justifyContent: 'space-between',
};

const navGroupLeft: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
};

const logoContainerStyle: React.CSSProperties = {
    marginRight: '30px',
    cursor: 'pointer',
    height: '40px',
};

const logoImageStyle: React.CSSProperties = {
    height: '100%',
    width: 'auto',
    verticalAlign: 'middle',
};

const navItemStyle: React.CSSProperties = {
    color: 'white',
    textDecoration: 'none',
    marginRight: '15px',
    fontSize: '0.9rem',
    opacity: 0.8,
    cursor: 'pointer',
    transition: 'opacity 0.2s',
};

const navItemHoverStyle = {
    opacity: 1,
    color: 'white',
};

const navGroupRight: React.CSSProperties = {
    display: 'flex',
    alignItems: 'center',
};

const iconStyle: React.CSSProperties = {
    fontSize: '1.5rem',
    marginRight: '20px',
    cursor: 'pointer',
};

const profileNameStyle: React.CSSProperties = {
    fontSize: '0.9rem',
    marginRight: '10px',
    cursor: 'pointer',
};

const profileAvatarStyle: React.CSSProperties = {
    width: '32px',
    height: '32px',
    borderRadius: '4px',
    cursor: 'pointer',
    overflow: 'hidden',
};

const dropdownMenuStyle: React.CSSProperties = {
    position: 'absolute',
    top: '65px',
    right: '20px',
    backgroundColor: '#000',
    border: '1px solid #333',
    borderRadius: '4px',
    boxShadow: '0 0 10px rgba(0,0,0,0.5)',
    padding: '10px 0',
    minWidth: '180px',
    zIndex: 15,
};

const dropdownItemStyle: React.CSSProperties = {
    padding: '10px 15px',
    cursor: 'pointer',
    fontSize: '0.9rem',
    color: 'white',
    transition: 'background-color 0.2s',
};


const Header: React.FC = () => {
    const navigate = useNavigate();
    const { currentUser, updateAvatar, logout } = useUser();

    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const userName = currentUser?.username || 'Invitado';
    const avatarUrl = currentUser?.avatarUrl || '/netflix.png';

    const handleEditProfile = (e: React.MouseEvent) => {
        e.stopPropagation();
        setIsDropdownOpen(false);
        setIsModalOpen(true);
    };

    const handleSelectAvatar = (url: string) => {
        if (currentUser) {
            updateAvatar(url);
            console.log(`[Backend Update] Guardando nuevo avatar ${url} para ${currentUser.username}`);
        }
    };

    const handleLogout = (e: React.MouseEvent) => {
        e.stopPropagation();
        logout();
        navigate("/");
    };

    const handleLogoClick = () => {
        navigate("/inicio");
    };

    const handleNavigation = (path: string) => {
        navigate(path);
    };

    const applyHoverStyle = (e: React.MouseEvent<HTMLSpanElement>, isHovering: boolean) => {
        if (isHovering) {
            Object.assign(e.currentTarget.style, navItemHoverStyle);
        } else {
            Object.assign(e.currentTarget.style, navItemStyle);
        }
    };


    return (
        <>
            <header style={headerStyle}>
                {/*logo nav*/}
                <div style={navGroupLeft}>
                    <div
                        style={logoContainerStyle}
                        onClick={handleLogoClick}
                    >
                        <img
                            src="/SpringFlix.png"
                            alt="SpringFlix Logo"
                            style={logoImageStyle}
                        />
                    </div>

                    {/* 1. Inicio (Redirige a /inicio) */}
                    <span
                        style={navItemStyle}
                        onClick={() => handleNavigation('/inicio')} // ✅ Enlace 'Inicio' navega a /inicio
                        onMouseEnter={(e) => applyHoverStyle(e, true)}
                        onMouseLeave={(e) => applyHoverStyle(e, false)}
                    >
                        Inicio
                    </span>

                    {/* 2. Películas (Redirige a /peliculas, que muestra el catálogo) */}
                    <span
                        style={navItemStyle}
                        onClick={() => handleNavigation('/peliculas')}
                        onMouseEnter={(e) => applyHoverStyle(e, true)}
                        onMouseLeave={(e) => applyHoverStyle(e, false)}
                    >
                        Películas
                    </span>

                    {/* 3. Mi Lista */}
                    <span
                        style={navItemStyle}
                        onClick={() => handleNavigation('/list')}
                        onMouseEnter={(e) => applyHoverStyle(e, true)}
                        onMouseLeave={(e) => applyHoverStyle(e, false)}
                    >
                        Mi Lista
                    </span>
                </div>

                {/* perfil usuario*/}
                <div style={navGroupRight}>
                    <IoIosSearch style={iconStyle} title="Buscar" onClick={() => console.log('Abrir búsqueda')} />
                    <IoIosNotifications style={iconStyle} title="Notificaciones" onClick={() => console.log('Ver notificaciones')} />

                    {/* 5. PERFIL Y DROPDOWN CONTAINER */}
                    <div
                        style={{ display: 'flex', alignItems: 'center', position: 'relative' }}
                        onClick={() => setIsDropdownOpen(!isDropdownOpen)}
                    >

                        {/* Nombre del Perfil: DINÁMICO */}
                        <span style={profileNameStyle}>{userName}</span>

                        {/* Foto de Perfil: DINÁMICA */}
                        <div style={profileAvatarStyle} title={userName}>
                            <img
                                src={avatarUrl}
                                alt={`Avatar de ${userName}`}
                                style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                            />
                        </div>

                        {/* Icono de Flecha para el Dropdown (perfil/configuración) */}
                        <IoIosArrowDown
                            style={{
                                fontSize: '1rem',
                                marginLeft: '5px',
                                cursor: 'pointer',
                                transform: isDropdownOpen ? 'rotate(180deg)' : 'rotate(0deg)',
                                transition: 'transform 0.2s'
                            }}
                        />

                        {/* MENÚ DESPLEGABLE */}
                        {isDropdownOpen && (
                            <div style={dropdownMenuStyle}>
                                <div
                                    style={dropdownItemStyle}
                                    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#1F1F1F'}
                                    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                                    onClick={handleEditProfile}
                                >
                                    Editar foto de perfil
                                </div>
                                <div
                                    style={dropdownItemStyle}
                                    onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#1F1F1F'}
                                    onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                                    onClick={handleLogout}
                                >
                                    Cerrar sesión
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </header>

            {/* MODAL DE SELECCIÓN DE AVATAR */}
            <AvatarModal
                isOpen={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                onSelectAvatar={handleSelectAvatar}
            />
        </>
    );
};

export default Header;