import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = ({ redirectPath = '/login' }) => {
    const isAuthenticated = !!localStorage.getItem('accessToken');

    return isAuthenticated ? <Outlet /> : <Navigate to={redirectPath} />;
};

export default ProtectedRoute;