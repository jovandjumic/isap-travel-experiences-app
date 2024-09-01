import React, { createContext, useState } from 'react';

const AuthContext = createContext();

const AuthProvider = ({ children }) => {

    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [userId, setUserId] = useState(null); // Dodato stanje za korisnički ID

    return (
        <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated, userId, setUserId }}>
            {children}
        </AuthContext.Provider>
    );
};

export { AuthProvider, AuthContext };
