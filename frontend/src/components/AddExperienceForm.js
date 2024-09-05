import React, { useEffect, useState } from 'react';
import api from '../services/api';
import './AddExperienceForm.css';
import { useNavigate } from 'react-router-dom';

const AddExperienceForm = () => {

    const [useCustomLocationType, setUseCustomLocationType] = useState(false);
const [customLocationType, setCustomLocationType] = useState('');
    const navigate = useNavigate();
    const [countries, setCountries] = useState([]); // State za čuvanje država
    const [formData, setFormData] = useState({
        destination: {
            locationName: '',
            regionArea: '',
            countryId: '',
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

    const handleCountryChange = (e) => {
        const selectedCountryId = e.target.value;
        setFormData((prevState) => ({
            ...prevState,
            destination: {
                ...prevState.destination,
                countryId: selectedCountryId  // Sada čuvaš samo ID države
            },
        }));
    };    

    useEffect(() => {
        // Dohvatanje lista država sa servera
        const fetchCountries = async () => {
            try {
                const response = await api.get('/countries'); // API poziv za dohvatanje država
                setCountries(response.data);
            } catch (error) {
                console.error("Failed to fetch countries:", error);
            }
        };
        fetchCountries();
    }, []);

    // Stanja za čuvanje privremenih slika koje treba da se uploaduju ili obrišu nakon submita
    const [pendingUploads, setPendingUploads] = useState([]);
    const [pendingRemovals, setPendingRemovals] = useState([]);


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

    const handleBackClick = () => {
        setPendingUploads([]);
        navigate('/experiences'); // Navigacija na početnu stranu
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
    
    const handleImageRemove = (index) => {
        // Uklanjanje URL-a iz formData.images
        setFormData((prevState) => {
            const newImages = [...prevState.images];
            newImages.splice(index, 1);
            return { ...prevState, images: newImages };
        });
    
        // Uklanjanje odgovarajućeg fajla iz pendingUploads
        setPendingUploads((prev) => {
            const newPendingUploads = [...prev];
            newPendingUploads.splice(index, 1); // Uklanja fajl na istom indexu
            return newPendingUploads;
        });
    };
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('accessToken');
        const config = {
            headers: { Authorization: `Bearer ${token}` }
        };
    
        try {
            // Kreiranje Experience objekta bez slika
            const { images, ...formDataWithoutImages } = formData; // Uklanjanje images polja pre slanja
            const response = await api.post('/experiences', formDataWithoutImages, config);
            const experienceId = response.data.id; // Pretpostavka da server vraća ID novog Experience objekta
    
            // Sada uploadujte slike i povežite ih sa kreiranim Experience-om
            const uploadedImageUrls = await Promise.all(pendingUploads.map(async (file) => {
                const uploadData = new FormData();
                uploadData.append('file', file);
                const uploadConfig = {
                    headers: { 
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'multipart/form-data'
                    }
                };
                const uploadResponse = await api.post(`/experiences/${experienceId}/images`, uploadData, uploadConfig);
                return uploadResponse.data.imageUrl; // Pretpostavka da server vraća URL slike
            }));
    
            // Ažuriranje Experience objekta sa URL-ovima slika
            const updatedFormData = {
                ...formDataWithoutImages,
                images: uploadedImageUrls, // Dodavanje samo stvarnih URL-ova slika
            };
            await api.put(`/experiences/${experienceId}`, updatedFormData, config);
    
            alert('Iskustvo uspešno dodato!');
            navigate('/experiences');
    
            // Reset stanja nakon uspešnog submita
            setPendingUploads([]);
            setFormData({
                destination: {
                    locationName: '',
                    regionArea: '',
                    countryId: '',
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
            }); // Reset formData nakon uspešnog dodavanja
        } catch (error) {
            console.error('Greška prilikom dodavanja iskustva:', error);
            alert('Dodavanje iskustva nije uspelo. Molimo pokušajte ponovo.');
        }
    };
    
    
    
    

    return (
        <div className="add-experience-form-container">
            <form onSubmit={handleSubmit} className="add-experience-form">
                <h2>Dodaj novo putovanje</h2>

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
    <label>Prevozno sredstvo:</label>
    <select 
        name="costs.travelMode" 
        value={formData.costs.travelMode} 
        onChange={handleChange}
        required
    >
        <option value="">Izaberite prevozno sredstvo</option>
        <option value="automobil">Automobil</option>
        <option value="autobus">Autobus</option>
        <option value="voz">Voz</option>
        <option value="avion">Avion</option>
        <option value="bicikl">Bicikl</option>
        <option value="pešice">Pešice</option>
        <option value="brod">Brod</option>
    </select>
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
    <select 
        name="destination.countryId" 
        value={formData.destination.countryId} 
        onChange={handleCountryChange}
    >
        <option value="">Izaberite državu</option>
        {countries.map(country => (
            <option key={country.id} value={country.id}>
                {country.countryName}
            </option>
        ))}
    </select>
</div>

                    <div className="form-group">
                    <label>Tip destinacije:</label>
                    <select
                        value={useCustomLocationType ? 'custom' : formData.destination.locationType}
                        onChange={(e) => {
                            const selectedValue = e.target.value;
                            if (selectedValue === 'custom') {
                                setUseCustomLocationType(true);
                                setCustomLocationType('');
                                setFormData((prevState) => ({
                                    ...prevState,
                                    destination: {
                                        ...prevState.destination,
                                        locationType: ''
                                    }
                                }));
                            } else {
                                setUseCustomLocationType(false);
                                setFormData((prevState) => ({
                                    ...prevState,
                                    destination: {
                                        ...prevState.destination,
                                        locationType: selectedValue
                                    }
                                }));
                            }
                        }}
                    >
        <option value="">Izaberite tip destinacije</option>
        <option value="Grad">Grad</option>
        <option value="Selo">Selo</option>
        <option value="Plaža">Plaža</option>
        <option value="Ostrvo">Ostrvo</option>
        <option value="Planina">Planina</option>
        <option value="Reka">Reka</option>
        <option value="Jezero">Reka</option>
        <option value="custom">Prilagođeno</option>
    </select>

    {useCustomLocationType && (
                <input className='customfield'
                    type="text" 
                    placeholder="Unesite tip destinacije" 
                    value={customLocationType} 
                    onChange={(e) => {
                        const customValue = e.target.value;
                        setCustomLocationType(customValue);
                        setFormData((prevState) => ({
                            ...prevState,
                            destination: {
                                ...prevState.destination,
                                locationType: customValue
                            }
                        }));
                    }}
                />
            )}
        </div>
                </div>

                <h3>Troškovi:</h3>
                <div className="form-row">
                    <div className="form-group">
                        <label>Troškovi prevoza:</label>
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

                <h3 className="section-header">Opis putovanja i fotografije:</h3>
                
                <div className="form-row">
                    <div className="form-group">
                        <label>Opis putovanja:</label>
                        <textarea 
                            name="description" 
                            value={formData.description} 
                            onChange={handleChange} 
                        />
                    </div>
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label>Fotografije:</label>
                            <div className="images-container">
                                <div className="file-input-wrapper">
                                    <input 
                                        type="file" 
                                        multiple 
                                        onChange={handleImageChange} 
                                        className="image-input"
                                    />
                                </div>
                                {formData.images.length > 0 && (
                                    <div className="images-list-container">
                                        {formData.images.map((imageUrl, index) => {
                                            // Provera da li je imageUrl blob URL ili pravi URL sa servera
                                            const isBlob = imageUrl.startsWith('blob:');
                                            const imageSrc = isBlob ? imageUrl : `${process.env.REACT_APP_IMAGE_URL}/uploads/${imageUrl.split('\\').pop()}`;
                                            return (
                                                <div key={index} className="image-preview-item">
                                                    <img src={imageSrc} alt={`Experience ${index}`} className="image-preview-thumbnail" />
                                                    <button type="button" className="remove-image-button" onClick={() => handleImageRemove(index)}>
                                                        &times;
                                                    </button>
                                                </div>
                                            );
                                        })}
                                    </div>
                                )}
                            </div>
                            </div>
                </div> 

                <button type="submit" className="submit-button">Dodaj Putovanje</button>
                <button type="button" className="back-button" onClick={handleBackClick}>Nazad</button>
            </form>
        </div>
    );
};

export default AddExperienceForm;
