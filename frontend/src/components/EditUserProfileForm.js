import React, { useState, useEffect, useContext } from 'react';
import api from '../services/api';
import './EditUserProfileForm.css';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContextProvider';

const EditUserProfileForm = () => {
    const { userId } = useContext(AuthContext);
    const navigate = useNavigate();
    
    const [formData, setFormData] = useState({
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        biography: '',
        profilePicture: ''
    });

    useEffect(() => {
        // Učitavanje podataka o korisniku
        const fetchUserProfile = async () => {
            try {
                const token = localStorage.getItem('accessToken');
                const config = {
                    headers: { Authorization: `Bearer ${token}` }
                };
                const response = await api.get(`/users/${userId}`, config);
                
                // Postavljanje podataka u formu
                setFormData({
                    username: response.data.username || '',
                    firstName: response.data.firstName || '',
                    lastName: response.data.lastName || '',
                    email: response.data.email || '',
                    phoneNumber: response.data.phoneNumber || '',
                    biography: response.data.biography || '',
                    profilePicture: response.data.profilePicture || ''
                });
            } catch (error) {
                console.error('Failed to fetch user profile:', error);
            }
        };

        if (userId) {
            fetchUserProfile();
        }
    }, [userId]);

    const handleChange = (e) => {
        const { name, value } = e.target;

        setFormData((prevState) => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        const imageUrl = URL.createObjectURL(file);
        setFormData((prevState) => ({
            ...prevState,
            profilePicture: imageUrl
        }));
    };

    const handleBackClick = () => {
        navigate(`/profile/${userId}`); // Navigacija na stranicu profila
    };

    const handlePasswordChange = () => {
        navigate(`/change-password`); // Navigacija na stranicu za promenu lozinke
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('accessToken');
            const config = {
                headers: { Authorization: `Bearer ${token}` }
            };
            await api.put(`/users/${userId}`, formData, config);
            alert('Podaci su uspešno izmenjeni!');
            navigate(`/profile/${userId}`); // Preusmeravanje na stranicu profila
        } catch (error) {
            console.error('Greška prilikom izmene podataka:', error);
            alert('Izmena podataka nije uspela. Molimo pokušajte ponovo.');
        }
    };

    return (
        <div className="edit-user-profile-form-container">
            <form onSubmit={handleSubmit} className="edit-user-profile-form">
                <h2>Izmeni korisničke podatke</h2>

                <div className="form-row">
                    <div className="form-group">
                        <label>Korisničko ime:</label>
                        <input 
                            type="text" 
                            name="username" 
                            value={formData.username} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>
                </div>
                <button type="button" className="change-password-button" onClick={handlePasswordChange}>Promeni lozinku</button>
                <div className="form-row">
                    <div className="form-group">
                        <label>Ime:</label>
                        <input 
                            type="text" 
                            name="firstName" 
                            value={formData.firstName} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>
                    <div className="form-group">
                        <label>Prezime:</label>
                        <input 
                            type="text" 
                            name="lastName" 
                            value={formData.lastName} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label>Email:</label>
                        <input 
                            type="email" 
                            name="email" 
                            value={formData.email} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>
                    <div className="form-group">
                        <label>Telefon:</label>
                        <input 
                            type="text" 
                            name="phoneNumber" 
                            value={formData.phoneNumber} 
                            onChange={handleChange} 
                        />
                    </div>
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label>Biografija:</label>
                        <textarea 
                            name="biography" 
                            value={formData.biography} 
                            onChange={handleChange} 
                        />
                    </div>
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label>Profilna slika:</label>
                        <input 
                            type="file" 
                            onChange={handleImageChange} 
                        />
                    </div>
                </div>

                <button type="submit" className="submit-button">Izmeni podatke</button>
                <button type="button" className="back-button" onClick={handleBackClick}>Nazad</button>
            </form>
        </div>
    );
};

export default EditUserProfileForm;
