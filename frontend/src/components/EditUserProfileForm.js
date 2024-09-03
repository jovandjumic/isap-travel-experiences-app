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

    const [pendingUpload, setPendingUpload] = useState(null);
    const [pendingRemoval, setPendingRemoval] = useState(null);

    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const token = localStorage.getItem('accessToken');
                const config = {
                    headers: { Authorization: `Bearer ${token}` }
                };
                const response = await api.get(`/users/${userId}`, config);

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

        setPendingUpload(file);
    };

    const handleImageRemove = () => {
        setPendingRemoval(true); // Obeležavamo da je potrebno obrisati trenutnu sliku
    
        setFormData((prevState) => ({
            ...prevState,
            profilePicture: '' // Uklanjamo sliku iz prikaza
        }));
    };

    const handleBackClick = () => {
        navigate(`/profile/${userId}`);
    };

    const handlePasswordChange = () => {
        navigate(`/change-password`);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('accessToken');
        const config = {
            headers: { Authorization: `Bearer ${token}` }
        };
    
        try {
            // Remove the old profile picture if a new one is selected
            if (pendingRemoval) {
                await api.delete(`/users/${userId}/profile-picture`, config);
            }
    
            // Upload the new profile picture if one is selected
            let uploadedImageUrl = formData.profilePicture; // Zadržavamo trenutni URL slike ako nema nove
            if (pendingUpload) {
                const imageFormData = new FormData();
                imageFormData.append('file', pendingUpload);
                const uploadResponse = await api.post(`/users/${userId}/profile-picture`, imageFormData, config);
                uploadedImageUrl = uploadResponse.data.split(': ')[1];
            }
    
            // Update the user's profile with the rest of the form data
            const updatedFormData = {
                ...formData,
                profilePicture: uploadedImageUrl // Ažuriramo URL slike ako je promenjena
            };
    
            await api.put(`/users/${userId}`, updatedFormData, config);
            alert('Podaci su uspešno izmenjeni!');
            navigate(`/profile/${userId}`);
    
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
                        {formData.profilePicture && (
                            <div className="image-container">
                                <img src={formData.profilePicture} alt="Profile" className="profile-picture-preview" />
                                <button type="button" className="remove-image-button" onClick={handleImageRemove}>Ukloni</button>
                            </div>
                        )}
                        <input 
                            type="file" 
                            onChange={handleImageChange} 
                            className="image-input"// Onemogućeno ako slika već postoji
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
