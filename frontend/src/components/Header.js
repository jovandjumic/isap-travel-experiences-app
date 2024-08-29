import React from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faSignInAlt, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import './Header.css';

const Header = () => {
    return (
        <header className="app-header">
            <div className="header-container">
                <div className="logo">
                    <Link to="/">
                        <img src="/logo.png" alt="Site Logo" className="logo-image" />
                    </Link>
                </div>
                <nav className="nav-links">
                    <Link to="/add-experience" className="nav-button">
                        <FontAwesomeIcon icon={faPlus} className="button-icon" />
                        <span>Dodaj Iskustvo</span>
                    </Link>
                    <Link to="/login" className="nav-button">
                        <FontAwesomeIcon icon={faSignInAlt} className="button-icon" />
                        <span>Prijava</span>
                    </Link>
                    <Link to="/register" className="nav-button">
                        <FontAwesomeIcon icon={faUserPlus} className="button-icon" />
                        <span>Registracija</span>
                    </Link>
                </nav>
            </div>
        </header>
    );
}

export default Header;
