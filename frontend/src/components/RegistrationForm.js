import React, { useState } from 'react';
import api from '../services/api'; // Uvoz API servisa za slanje zahteva
import './RegistrationForm.css'; // CSS za stilizaciju

const RegistrationForm = () => {
    const [formData, setFormData] = useState({
        firstname: '',
        lastname: '',
        email: '',
        username: '',
        password: ''
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/auth/register', formData);
            alert('Registracija uspešna!');
            // Možete dodati logiku za preusmeravanje korisnika ili čišćenje forme
        } catch (error) {
            console.error('Greška prilikom registracije:', error);
            alert('Registracija nije uspela. Molimo pokušajte ponovo.');
        }
    };

    return (
        <div className="registration-form-container">
            <form onSubmit={handleSubmit} className="registration-form">
                <h2>Registracija</h2>
                <div className="form-group">
                    <label htmlFor="firstname">Ime:</label>
                    <input 
                        type="text" 
                        id="firstname" 
                        name="firstname" 
                        value={formData.firstname} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="lastname">Prezime:</label>
                    <input 
                        type="text" 
                        id="lastname" 
                        name="lastname" 
                        value={formData.lastname} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input 
                        type="email" 
                        id="email" 
                        name="email" 
                        value={formData.email} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
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
                <button type="submit" className="submit-button">Registruj se</button>
            </form>
        </div>
    );
};

export default RegistrationForm;
