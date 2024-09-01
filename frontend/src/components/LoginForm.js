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

    const {setIsAuthenticated, userId, setUserId} = useContext(AuthContext);
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
            try {
                const token = localStorage.getItem('accessToken');
                console.log('Token from localStorage:', token);  // Log token from localStorage
                const config = {
                    headers: { Authorization: `Bearer ${token}` }
                };
                const userResponse = await api.get('auth/me', config);
                setUserId(userResponse.data.id);
                console.log('User ID:', userResponse.data.id); // Log the user ID
            } catch (error) {
                console.error('Failed to fetch current user:', error);
                alert('Failed to fetch current user');
            }
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
        <div className="login-form-container-login">
    <form onSubmit={handleSubmit} className="login-form-login">
        <h2>Prijava</h2>
        <div className="form-group-login">
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
        <div className="form-group-login">
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
        <button type="submit" className="submit-button-login">Prijavi se</button>
        <button type="button" className="back-button-login" onClick={handleBackClick}>Nazad</button>
    </form>
</div>

    );
};

export default LoginForm;
