import React, { createContext, useContext, useState, type ReactNode, useEffect } from 'react';
export interface UsuarioDTO {
    id: number;
    username?: string;
    email?: string;
    rol: string;
}

export interface UserProfile extends UsuarioDTO {
    avatarUrl: string;
}

const STORAGE_KEY = 'springflixUser';
const AVATAR_MAP_KEY = 'springflixAvatars';

// Persistencia de profile pics
const getStoredAvatars = (): Record<number, string> => {
    try {
        const data = localStorage.getItem(AVATAR_MAP_KEY);
        return data ? JSON.parse(data) : {};
    } catch (e) {
        console.error("Error al cargar avatares persistentes:", e);
        return {};
    }
};

const setStoredAvatar = (userId: number, url: string) => {
    const avatars = getStoredAvatars();
    avatars[userId] = url;
    localStorage.setItem(AVATAR_MAP_KEY, JSON.stringify(avatars));
};


interface UserContextType {
    currentUser: UserProfile | null;
    login: (user: UsuarioDTO, avatarUrl: string) => void;
    logout: () => void;
    updateAvatar: (url: string) => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

interface UserProviderProps {
    children: ReactNode;
}

export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
    const [currentUser, setCurrentUser] = useState<UserProfile | null>(() => {
        try {
            const storedUser = localStorage.getItem(STORAGE_KEY);
            return storedUser ? JSON.parse(storedUser) : null;
        } catch (error) {
            console.error("Error al leer localStorage:", error);
            return null;
        }
    });

    useEffect(() => {
        if (currentUser) {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(currentUser));
        } else {
            localStorage.removeItem(STORAGE_KEY);
        }
    }, [currentUser]);

    const login = (user: UsuarioDTO, avatarUrl: string) => {
        const profile: UserProfile = {
            ...user,
            avatarUrl: avatarUrl,
        };
        setCurrentUser(profile);
    };

    const logout = () => {
        setCurrentUser(null);
    };

    const updateAvatar = (url: string) => {
        if (currentUser) {
            setCurrentUser({ ...currentUser, avatarUrl: url });
            setStoredAvatar(currentUser.id, url);
        }
    };

    return (
        <UserContext.Provider value={{ currentUser, login, logout, updateAvatar }}>
            {children}
        </UserContext.Provider>
    );
};

export const useUser = () => {
    const context = useContext(UserContext);
    if (context === undefined) {
        throw new Error('useUser must be used within a UserProvider');
    }
    return context;
};