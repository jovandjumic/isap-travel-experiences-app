// Header.js
import React, { useContext } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser, faPlus, faSignInAlt, faUserPlus, faSignOutAlt, faHome } from '@fortawesome/free-solid-svg-icons';
import './Header.css';
import { AuthContext } from '../contexts/AuthContextProvider';
import logo from '../assets/images/logo.png';
import api from '../services/api';

const Header = () => {

    const {isAuthenticated, setIsAuthenticated, userId, setUserId} = useContext(AuthContext);
    const navigate = useNavigate();
    const location = useLocation();

    const handleLogout = async (e) => {
        e.preventDefault();
        try {
            // Send a GET request to the logout endpoint
            await api.get('auth/logout');
            
            // Clear tokens from local storage
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            
            // Update the authentication state
            setIsAuthenticated(false);
            setUserId(null);
            
            // Notify the user
            alert('Odjava uspešna!');
            
            // Redirect to a specific page, for example, experiences
            const redirectTo = location.state?.from?.pathname || '/experiences';
            navigate(redirectTo, { replace: true });
        } catch (error) {
            console.error('Error during logout:', error);
            // Optionally, display an error message to the user
        }
    };

    const handleClick = () => {
        if (isAuthenticated) {
            navigate('/add-experience');
        } else {
            navigate('/login');
        }
    };

    const handleHomePage = () => {
            navigate('/');
    };

    const handleProfileClick = () => {
        navigate(`/users/${userId}`);
    };

    return (
        <header className="app-header">
            <div className="header-container">
                <div className="logo">
                    <Link to="/">
                        <img src={logo} alt="Site Logo" className="logo-image" />
                    </Link>
                </div>
                <nav className="nav-links">
                    <button className="nav-button" onClick={handleHomePage}>
                        <FontAwesomeIcon icon={faHome} className="button-icon" />
                        <span>Početna strana</span>
                    </button>
                    <button className="nav-button" onClick={handleClick}>
                        <FontAwesomeIcon icon={faPlus} className="button-icon" />
                        <span>Dodaj Putovanje</span>
                    </button>
                    {isAuthenticated && (
                        <button onClick={handleProfileClick} className="nav-button">
                        <FontAwesomeIcon icon={faUser} className="button-icon" />
                        <span>Moj Profil</span>
                    </button>
                    )}
                    {!isAuthenticated && (
                    <Link to="/register" className="nav-button">
                        <FontAwesomeIcon icon={faUserPlus} className="button-icon" />
                        <span>Registracija</span>
                    </Link>)}
                    {isAuthenticated ? (
                        <button onClick={handleLogout} className="nav-button">
                            <FontAwesomeIcon icon={faSignOutAlt} className="button-icon" />
                            <span>Odjava</span>
                        </button>
                    ) : (
                        <Link to="/login" className="nav-button">
                            <FontAwesomeIcon icon={faSignInAlt} className="button-icon" />
                            <span>Prijava</span>
                        </Link>
                    )}
                </nav>
            </div>
        </header>
    );
}

export default Header;
