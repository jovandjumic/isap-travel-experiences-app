import React, { useState } from 'react';
import api from '../services/api';
import './AddExperienceForm.css';
import { useNavigate } from 'react-router-dom';

const AddExperienceForm = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        destination: {
            locationName: '',
            regionArea: '',
            country: {
                countryName: '',
            },
            locationType: ''
        },
        daysSpent: '',
        costs: {
            travelCost: '',
            travelMode: '',
            travelRoute: '',
            accommodationCost: '',
            otherCosts: ''
        },
        numberOfPeople: '',
        images: [],
        description: ''
    });

    // Stanja za čuvanje privremenih slika koje treba da se uploaduju nakon submita
    const [pendingUploads, setPendingUploads] = useState([]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        const keys = name.split('.');

        setFormData((prevState) => {
            const updatedData = { ...prevState };

            if (keys.length === 1) {
                updatedData[keys[0]] = value;
            } else if (keys.length === 2) {
                updatedData[keys[0]][keys[1]] = value;
            } else if (keys.length === 3) {
                updatedData[keys[0]][keys[1]][keys[2]] = value;
            }

            return updatedData;
        });
    };

    const handleImageChange = (e) => {
        const files = Array.from(e.target.files);
        const newImageUrls = files.map(file => URL.createObjectURL(file));

        setFormData((prevState) => ({
            ...prevState,
            images: [...prevState.images, ...newImageUrls],
        }));

        // Čuvanje file-ova za upload nakon submita
        setPendingUploads((prev) => [...prev, ...files]);
    };

    const handleBackClick = () => {
        navigate('/experiences'); // Navigacija na početnu stranu
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('accessToken');
        const config = {
            headers: { Authorization: `Bearer ${token}` }
        };

        try {
            // Prvo kreirajte iskustvo bez slika
            const response = await api.post('/experiences', formData, config);
            const experienceId = response.data.id; // Pretpostavljamo da se vraća ID kreiranog iskustva

            // Zatim upload-ujte nove slike
            const uploadedImageUrls = await Promise.all(pendingUploads.map(async (file) => {
                const formData = new FormData();
                formData.append('file', file);
                const uploadConfig = {
                    headers: { 
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'multipart/form-data'
                    }
                };
                const uploadResponse = await api.post(`/experiences/${experienceId}/images`, formData, uploadConfig);
                return uploadResponse.data.split(": ")[1]; // Dobijamo samo URL
            }));

            // Ažurirajte experience sa slikama
            setFormData((prevState) => ({
                ...prevState,
                images: [...prevState.images, ...uploadedImageUrls],
            }));

            alert('Iskustvo uspešno dodato!');
            navigate('/experiences'); // Preusmeravanje na listu iskustava

            // Reset stanja nakon uspešnog submita
            setPendingUploads([]);
        } catch (error) {
            console.error('Greška prilikom dodavanja iskustva:', error);
            alert('Dodavanje iskustva nije uspelo. Molimo pokušajte ponovo.');
        }
    };

    return (
        <div className="add-experience-form-container">
            <form onSubmit={handleSubmit} className="add-experience-form">
                <h2>Dodaj novo iskustvo</h2>

                <div className="form-row">
                    <div className="form-group">
                        <label>Broj dana:</label>
                        <input 
                            type="number" 
                            name="daysSpent" 
                            value={formData.daysSpent} 
                            onChange={handleChange} 
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Broj osoba:</label>
                        <input 
                            type="number" 
                            name="numberOfPeople" 
                            value={formData.numberOfPeople} 
                            onChange={handleChange} 
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Način putovanja:</label>
                        <input 
                            type="text" 
                            name="costs.travelMode" 
                            value={formData.costs.travelMode} 
                            onChange={handleChange} 
                        />
                    </div>
                    <div className="form-group">
                        <label>Putna ruta:</label>
                        <input 
                            type="text" 
                            name="costs.travelRoute" 
                            value={formData.costs.travelRoute} 
                            onChange={handleChange} 
                        />
                    </div>
                </div>

                <h3>Destinacija:</h3>
                <div className="form-row">
                    <div className="form-group">
                        <label>Naziv destinacije:</label>
                        <input 
                            type="text" 
                            name="destination.locationName" 
                            value={formData.destination.locationName} 
                            onChange={handleChange} 
                            required 
                        />
                    </div>
                    <div className="form-group">
                        <label>Oblast/Regija:</label>
                        <input 
                            type="text" 
                            name="destination.regionArea" 
                            value={formData.destination.regionArea} 
                            onChange={handleChange} 
                        />
                    </div>
                    <div className="form-group">
                        <label>Država:</label>
                        <input 
                            type="text" 
                            name="destination.country.countryName" 
                            value={formData.destination.country.countryName} 
                            onChange={handleChange} 
                        />
                    </div>
                    <div className="form-group">
                        <label>Tip destinacije:</label>
                        <input 
                            type="text" 
                            name="destination.locationType" 
                            value={formData.destination.locationType} 
                            onChange={handleChange} 
                        />
                    </div>
                </div>

                <h3>Troškovi:</h3>
                <div className="form-row">
                    <div className="form-group">
                        <label>Troškovi putovanja:</label>
                        <input 
                            type="number" 
                            name="costs.travelCost" 
                            value={formData.costs.travelCost} 
                            onChange={handleChange} 
                        />
                    </div>
                    <div className="form-group">
                        <label>Troškovi smeštaja:</label>
                        <input 
                            type="number" 
                            name="costs.accommodationCost" 
                            value={formData.costs.accommodationCost} 
                            onChange={handleChange} 
                        />
                    </div>
                    <div className="form-group">
                        <label>Ostali troškovi:</label>
                        <input 
                            type="number" 
                            name="costs.otherCosts" 
                            value={formData.costs.otherCosts} 
                            onChange={handleChange} 
                        />
                    </div>
                </div>
                
                <h3>Opis putavnja i fotografije:</h3>
                <div className="form-row">
                    <div className="form-group">
                        <label>Opis putovanja:</label>
                        <textarea 
                            name="description" 
                            value={formData.description} 
                            onChange={handleChange} 
                        />
                    </div>
                    <div className="images-container">
                        {formData.images.map((imageUrl, index) => (
                            <div key={index} className="image-preview">
                                <img src={imageUrl} alt={`Experience ${index}`} />
                            </div>
                        ))}
                        <div className="form-group">
                            <label>Fotografije:</label>
                            <input 
                                type="file" 
                                multiple 
                                onChange={handleImageChange} 
                            />
                        </div>
                    </div>
                </div>

                <button type="submit" className="submit-button">Dodaj Iskustvo</button>
                <button type="button" className="back-button" onClick={handleBackClick}>Nazad</button>
            </form>
        </div>
    );
};

export default AddExperienceForm;
