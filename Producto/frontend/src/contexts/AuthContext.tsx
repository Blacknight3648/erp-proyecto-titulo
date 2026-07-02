import { createContext, useContext, useState, ReactNode } from 'react';

export interface User {
    email: string;
    name: string;
    role?: string;
}

export interface AuthContextType {
    isAuthenticated: boolean;
    user: User | null;
    login: (email: string, password: string) => boolean;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [user, setUser] = useState<User | null>(null);

    const login = (email: string, password: string) => {
        // Credenciales de prueba: admin / admin
        if (email === 'admin' && password === 'admin') {
            const nuevoUser = { email, name: 'Administrador' };
            setIsAuthenticated(true);
            setUser(nuevoUser);
            localStorage.setItem('usuarioActual', nuevoUser.name);
            return true;
        }
        return false;
    };

    const logout = () => {
        setIsAuthenticated(false);
        setUser(null);
        localStorage.removeItem('usuarioActual');
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);
