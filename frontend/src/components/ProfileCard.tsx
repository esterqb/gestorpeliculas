import React, { useState } from 'react';

interface ProfileCardProps {
    nombre: string;
    imagenUrl?: string;
    esEspecial?: boolean;
    onClick: () => void;
}

const cardStyle: React.CSSProperties = {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    margin: '0 20px',
    cursor: 'pointer',
    transition: 'transform 0.3s',
    maxWidth: '180px',
};

const avatarContainerStyle: React.CSSProperties = {
    width: '150px',
    height: '150px',
    borderRadius: '4px',
    overflow: 'hidden',
    border: '3px solid transparent',
};

const avatarImageStyle: React.CSSProperties = {
    width: '100%',
    height: '100%',
    objectFit: 'cover',
};

const avatarIconStyle: React.CSSProperties = {
    width: '100%',
    height: '100%',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '3rem',
    backgroundColor: '#333',
};


const nameStyle: React.CSSProperties = {
    marginTop: '10px',
    fontSize: '1.2rem',
    color: '#808080',
    fontWeight: '400',
};

    const ProfileCard: React.FC<ProfileCardProps> = ({ nombre, imagenUrl, esEspecial, onClick }) => {
        const [isHovered, setIsHovered] = useState(false);

        const currentAvatarStyle = {
            ...avatarContainerStyle,
            transform: isHovered ? 'scale(1.05)' : 'scale(1)',
            borderColor: isHovered ? 'white' : 'transparent',
        };
        const currentNameStyle = {
            ...nameStyle,
            color: isHovered ? 'white' : '#808080',
        };

        return (
            <div
                style={cardStyle}
                onMouseEnter={() => setIsHovered(true)}
                onMouseLeave={() => setIsHovered(false)}
                onClick={onClick}
            >
                <div style={currentAvatarStyle}>
                    {esEspecial === true ? (
                        <div style={avatarIconStyle}>+</div>
                    ) : (
                        <img src={imagenUrl} alt={nombre} style={avatarImageStyle} />
                    )}
                </div>
                <div style={currentNameStyle}>
                    {nombre}
                </div>
            </div>
        );
    };

    export default ProfileCard;