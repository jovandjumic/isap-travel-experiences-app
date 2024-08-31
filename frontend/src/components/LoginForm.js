import React, { useContext, useState } from 'react';
import api from '../services/api'; // Uvoz API servisa za slanje zahteva
import './LoginForm.css';
import { useLocation, useNavigate } from 'react-router-dom'; // CSS za stilizaciju
import { AuthContext } from '../contexts/AuthContextProvider';

const LoginForm = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });

    const {isAuthenticated, setIsAuthenticated} = useContext(AuthContext);
    const navigate = useNavigate();
    const location = useLocation();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/auth/authenticate', formData);
            const { access_token, refresh_token } = response.data;
            // Možete sačuvati token u localStorage ili u stanju aplikacije
            localStorage.setItem('accessToken', access_token);
            localStorage.setItem('refreshToken', refresh_token);
            setIsAuthenticated(true);
            alert('Prijava uspešna!');
            const redirectTo = location.state?.from?.pathname || '/experiences';
            navigate(redirectTo, { replace: true });
            // Možete dodati logiku za preusmeravanje korisnika na drugu stranicu
        } catch (error) {
            console.error('Greška prilikom prijave:', error);
            alert('Prijava nije uspela. Molimo pokušajte ponovo.');
        }
    };

    const handleBackClick = () => {
        navigate('/experiences'); // Navigacija na početnu stranu
    };

    return (
        <div className="login-form-container">
            <form onSubmit={handleSubmit} className="login-form">
                <h2>Prijava</h2>
                <div className="form-group">
                    <label htmlFor="username">Korisničko ime:</label>
                    <input 
                        type="text" 
                        id="username" 
                        name="username" 
                        value={formData.username} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Lozinka:</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        value={formData.password} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <button type="submit" className="submit-button">Prijavi se</button>
                <button type="button" className="back-button" onClick={handleBackClick}>Nazad</button>
            </form>
        </div>
    );
};

export default LoginForm;
