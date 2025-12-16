import React from 'react';

// Lista de fotos disponibles de perfil
const AVATAR_OPTIONS = [
    '/aggretsuko.jpg',
    '/eleven.jpg',
    '/merlina.jpg',
    '/netflix.png',
    '/squid.jpeg',
    '/thing.jpg'
];

interface AvatarModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSelectAvatar: (url: string) => void;
}

const modalOverlayStyle: React.CSSProperties = {
    position: 'fixed',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundColor: 'rgba(0, 0, 0, 0.75)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 20,
};

const modalContentStyle: React.CSSProperties = {
    backgroundColor: '#141414',
    padding: '30px',
    borderRadius: '8px',
    width: '90%',
    maxWidth: '600px',
    boxShadow: '0 4px 12px rgba(0, 0, 0, 0.5)',
    color: 'white',
};

const avatarGridStyle: React.CSSProperties = {
    display: 'flex',
    flexWrap: 'wrap',
    gap: '20px',
    justifyContent: 'center',
    marginTop: '20px',
};

const avatarOptionStyle: React.CSSProperties = {
    width: '100px',
    height: '100px',
    borderRadius: '4px',
    overflow: 'hidden',
    cursor: 'pointer',
    border: '3px solid transparent',
    transition: 'border-color 0.2s',
};


const AvatarModal: React.FC<AvatarModalProps> = ({ isOpen, onClose, onSelectAvatar }) => {
    if (!isOpen) return null;

    const handleAvatarClick = (url: string) => {
        onSelectAvatar(url);
        onClose();
    };

    return (
        <div style={modalOverlayStyle} onClick={onClose}>
            <div style={modalContentStyle} onClick={(e) => e.stopPropagation()}>
                <h2 style={{ color: '#E50914', marginBottom: '20px' }}>Elige tu nuevo avatar</h2>

                <div style={avatarGridStyle}>
                    {AVATAR_OPTIONS.map((url) => (
                        <div
                            key={url}
                            style={avatarOptionStyle}
                            // Añadimos un efecto hover simple
                            onMouseEnter={(e) => e.currentTarget.style.borderColor = 'white'}
                            onMouseLeave={(e) => e.currentTarget.style.borderColor = 'transparent'}
                            onClick={() => handleAvatarClick(url)}
                        >
                            <img
                                src={url}
                                alt="Avatar"
                                style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                            />
                        </div>
                    ))}
                </div>

                <button
                    onClick={onClose}
                    style={{ marginTop: '30px', padding: '10px 20px', backgroundColor: '#333', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}
                >
                    Cerrar
                </button>
            </div>
        </div>
    );
};

export default AvatarModal;