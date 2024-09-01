import React, { useState, useEffect } from 'react';
import api from '../services/api';
import './AddExperienceForm.css';
import { useNavigate, useParams } from 'react-router-dom';

const EditExperienceForm = () => {
    const navigate = useNavigate();
    const { id } = useParams(); // Preuzimanje ID-a iz URL-a
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

    useEffect(() => {
        // Učitavanje podataka o iskustvu po ID-u
        const fetchExperience = async () => {
            try {
                const response = await api.get(`/experiences/${id}`);
                
                // Postavljanje samo relevantnih podataka u formData
                const experienceData = {
                    destination: {
                        locationName: response.data.destination?.locationName || '',
                        regionArea: response.data.destination?.regionArea || '',
                        country: {
                            countryName: response.data.destination?.country?.countryName || '',
                        },
                        locationType: response.data.destination?.locationType || ''
                    },
                    daysSpent: response.data.daysSpent || '',
                    costs: {
                        travelCost: response.data.costs?.travelCost || '',
                        travelMode: response.data.costs?.travelMode || '',
                        travelRoute: response.data.costs?.travelRoute || '',
                        accommodationCost: response.data.costs?.accommodationCost || '',
                        otherCosts: response.data.costs?.otherCosts || ''
                    },
                    numberOfPeople: response.data.numberOfPeople || '',
                    images: response.data.images || []
                };
                
                setFormData(experienceData);
            } catch (error) {
                console.error('Failed to fetch experience:', error);
            }
        };

        fetchExperience();
    }, [id]);

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
        const imageUrls = files.map(file => URL.createObjectURL(file));
        setFormData((prevState) => ({
            ...prevState,
            images: [...prevState.images, ...imageUrls],
        }));
    };

    const handleBackClick = () => {
        navigate('/experiences'); // Navigacija na početnu stranu
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('accessToken');
            const config = {
                headers: { Authorization: `Bearer ${token}` }
            };
            await api.put(`experiences/${id}`, formData, config);
            alert('Iskustvo uspešno izmenjeno!');
            navigate('/experiences'); // Preusmeravanje na listu iskustava
        } catch (error) {
            console.error('Greška prilikom izmene iskustva:', error);
            alert('Izmena iskustva nije uspela. Molimo pokušajte ponovo.');
        }
    };

    return (
        <div className="add-experience-form-container">
            <form onSubmit={handleSubmit} className="add-experience-form">
                <h2>Izmeni iskustvo</h2>

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

                <button type="submit" className="submit-button">Izmeni Iskustvo</button>
                <button type="button" className="back-button" onClick={handleBackClick}>Nazad</button>
            </form>
        </div>
    );
};

export default EditExperienceForm;
