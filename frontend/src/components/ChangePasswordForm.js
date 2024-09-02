import React, { useState } from 'react';
import api from '../services/api'; // Uvoz API servisa za slanje zahteva
import './ChangePasswordForm.css'; // Stilizacija komponente
import { useNavigate } from 'react-router-dom';

const ChangePasswordForm = () => {
    const [formData, setFormData] = useState({
        currentPassword: '',
        newPassword: '',
        confirmationPassword: ''
    });

    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('accessToken');
            const config = {
                headers: { Authorization: `Bearer ${token}` }
            };
            await api.patch('users', formData, config);
            alert('Lozinka uspešno izmenjena!');
            navigate('/profile'); // Navigacija na profil ili drugu stranicu po potrebi
        } catch (error) {
            console.error('Greška prilikom izmene lozinke:', error);
            alert('Izmena lozinke nije uspela. Molimo pokušajte ponovo.');
        }
    };

    const handleBackClick = () => {
        navigate('/profile'); // Navigacija nazad na profil ili početnu stranicu
    };

    return (
        <div className="change-password-form-container">
            <form onSubmit={handleSubmit} className="change-password-form">
                <h2>Izmena lozinke</h2>
                <div className="form-group">
                    <label htmlFor="currentPassword">Trenutna lozinka:</label>
                    <input 
                        type="password" 
                        id="currentPassword" 
                        name="currentPassword" 
                        value={formData.currentPassword} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="newPassword">Nova lozinka:</label>
                    <input 
                        type="password" 
                        id="newPassword" 
                        name="newPassword" 
                        value={formData.newPassword} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="confirmationPassword">Potvrda nove lozinke:</label>
                    <input 
                        type="password" 
                        id="confirmationPassword" 
                        name="confirmationPassword" 
                        value={formData.confirmationPassword} 
                        onChange={handleChange} 
                        required 
                    />
                </div>
                <button type="submit" className="submit-button">Izmeni lozinku</button>
                <button type="button" className="back-button" onClick={handleBackClick}>Nazad</button>
            </form>
        </div>
    );
};

export default ChangePasswordForm;
