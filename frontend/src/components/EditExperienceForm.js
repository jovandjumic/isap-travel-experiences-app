import React, { useState, useEffect } from 'react';
import api from '../services/api';
import './AddExperienceForm.css';
import { useNavigate, useParams } from 'react-router-dom';

const EditExperienceForm = () => {
    const [useCustomLocationType, setUseCustomLocationType] = useState(false);
const [customLocationType, setCustomLocationType] = useState('');
    const navigate = useNavigate();
    const [countries, setCountries] = useState([]); // State za čuvanje država
    const { id } = useParams(); // Preuzimanje ID-a iz URL-a
    const [formData, setFormData] = useState({
        destination: {
            locationName: '',
            regionArea: '',
            country: {
                countryName: '',
                continent: '',
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

    // Stanja za čuvanje privremenih slika koje treba da se uploaduju ili obrišu nakon submita
    const [pendingUploads, setPendingUploads] = useState([]);
    const [pendingRemovals, setPendingRemovals] = useState([]);

    useEffect(() => {
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
                    images: response.data.images || [],
                    description: response.data.description || ''
                };
                
                setFormData(experienceData);
            } catch (error) {
                console.error('Failed to fetch experience:', error);
            }
        };

        fetchExperience();
    }, [id]);

    const handleCountryChange = (e) => {
        const selectedCountryName = e.target.value;
        const selectedCountry = countries.find(
            (country) => country.countryName === selectedCountryName
        );
    
        if (selectedCountry) {
            setFormData((prevState) => ({
                ...prevState,
                destination: {
                    ...prevState.destination,
                    country: {
                        countryName: selectedCountry.countryName,
                        continent: selectedCountry.continent, // Postavi kontinent automatski
                    },
                },
            }));
        }
    };
    

    const handleBackClick = () => {
        // Resetovanje pending stanja prilikom povratka
        setPendingUploads([]);
        setPendingRemovals([]);
        navigate('/experiences');
    };

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
    
        // Kreiraj Blob URL-ove samo za nove slike
        const newImageUrls = files.map(file => URL.createObjectURL(file));
    
        // Dodavanje novih slika (samo Blob URL-ovi za prikaz)
        setFormData((prevState) => ({
            ...prevState,
            images: [
                ...prevState.images.filter(imageUrl => !imageUrl.startsWith('blob:')), // Sačuvaj postojeće slike koje nisu Blob
                ...newImageUrls
            ],
        }));
    
        // Čuvanje samo stvarnih fajlova za upload u pendingUploads
        setPendingUploads((prev) => [...prev, ...files]);
    };
    
    const handleImageRemove = (index) => {
        const removedImageUrl = formData.images[index];
    
        setFormData((prevState) => {
            const newImages = [...prevState.images];
            newImages.splice(index, 1);
            return { ...prevState, images: newImages };
        });
    
        if (removedImageUrl.startsWith('blob:')) {
            // Ako je Blob URL, uklonite odgovarajući fajl sa liste za upload
            setPendingUploads((prev) => {
                const uploadIndex = index - (formData.images.length - prev.length);
                if (uploadIndex >= 0 && uploadIndex < prev.length) {
                    const newPendingUploads = [...prev];
                    newPendingUploads.splice(uploadIndex, 1);
                    return newPendingUploads;
                }
                return prev;
            });
        } else {
            // Ako nije Blob, dodajte URL na listu za brisanje
            setPendingRemovals((prev) => [...prev, removedImageUrl]);
        }
    };
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        const experienceId = id; // ID iskustva koje se uređuje
        const token = localStorage.getItem('accessToken');
    
        try {
            const config = {
                headers: { Authorization: `Bearer ${token}` }
            };
    
            // 1. Brisanje slika koje su već na serveru i koje su obeležene za brisanje
            await Promise.all(pendingRemovals.map(async (imageUrl) => {
                await api.delete(`/experiences/${experienceId}/images`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'text/plain'
                    },
                    data: imageUrl,
                });
            }));
    
            // 2. Uploadovanje novih slika koje su dodate u pendingUploads
            const uploadedImageUrls = await Promise.all(pendingUploads.map(async (file) => {
                const uploadData = new FormData();
                uploadData.append('file', file);
                const response = await api.post(`/experiences/${experienceId}/images`, uploadData, config);
                return response.data.imageUrl; // Pretpostavka da server vraća URL slike
            }));
    
            // 3. Ažuriranje `formData` sa URL-ovima novih slika (sačuvajte postojeće slike koje nisu uklonjene)
            const updatedImages = [
                ...formData.images.filter(imageUrl => !pendingRemovals.includes(imageUrl) && !imageUrl.startsWith('blob:')), 
                ...uploadedImageUrls
            ];
    
            const updatedFormData = {
                ...formData,
                images: updatedImages,
            };
    
            // 4. Slanje PUT zahteva za ažuriranje iskustva sa novim i postojećim slikama
            await api.put(`/experiences/${experienceId}`, updatedFormData, config);
            alert('Iskustvo uspešno izmenjeno!');
            navigate('/experiences');
    
            // 5. Resetovanje stanja nakon uspešnog slanja podataka
            setPendingUploads([]);
            setPendingRemovals([]);
        } catch (error) {
            console.error('Greška prilikom izmene iskustva:', error);
            alert('Izmena iskustva nije uspela. Molimo pokušajte ponovo.');
        }
    };
    
    
    
    
    

    return (
        <div className="add-experience-form-container">
    <form onSubmit={handleSubmit} className="add-experience-form">
        <h2>Izmeni podatke o putovanju</h2>

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
        name="destination.country.countryName" 
        value={formData.destination.country.countryName} 
        onChange={handleCountryChange} 
        required
    >
        <option value="">Izaberite državu</option>
        {countries.map(country => (
            <option key={country.id} value={country.countryName}>
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

        <h3 className="section-header">Opis putovanja i fotografije:</h3> {/* Dodajemo crni header iznad */}
        
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
                                            const imageSrc = isBlob ? imageUrl : `http://localhost:8080/uploads/${imageUrl.split('\\').pop()}`;
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

        <button type="submit" className="submit-button">Izmeni putovanje</button>
        <button type="button" className="back-button" onClick={handleBackClick}>Nazad</button>
    </form>
</div>

    );
};

export default EditExperienceForm;
