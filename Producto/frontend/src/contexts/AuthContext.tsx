import { createContext, useContext, useState } from 'react';

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [user, setUser] = useState(null);

    const login = (email, password) => {
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
