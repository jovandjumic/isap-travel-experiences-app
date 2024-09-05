import React, { useState, useEffect } from 'react';
import api from '../services/api';
import './Filters.css';

const Filters = ({ onFilterChange }) => {
    const [minDays, setMinDays] = useState('');
    const [maxDays, setMaxDays] = useState('');
    const [minCost, setMinCost] = useState('');
    const [maxCost, setMaxCost] = useState('');
    const [priceType, setPriceType] = useState('total');
    const [destination, setDestination] = useState({
        locationName: '',
        regionArea: '',
        countryId: '',
        locationType: ''
    });
    const [sortField, setSortField] = useState('createdAt');
    const [sortOrder, setSortOrder] = useState('desc');
    const [pageSize, setPageSize] = useState(10);
    const [countries, setCountries] = useState([]); // Lista država sa backend-a
    const [customLocationType, setCustomLocationType] = useState('');
    const [useCustomLocationType, setUseCustomLocationType] = useState(false);

    useEffect(() => {
        // Kada se priceType promeni, ažuriraj sortField ako je trenutni sortField 'cost'
        if (sortField === 'costs.totalCost' || sortField === 'costPerPerson') {
            setSortField(priceType === 'total' ? 'costs.totalCost' : 'costPerPerson');
        }
    }, [priceType]);

    useEffect(() => {
        // Dohvatanje liste država sa backend-a
        const fetchCountries = async () => {
            try {
                const response = await api.get('/countries'); // API poziv za dohvatanje država
                setCountries(response.data);
            } catch (error) {
                console.error('Failed to fetch countries:', error);
            }
        };
        fetchCountries();
    }, []);

    const handleApplyFilter = () => {
        const filters = {
            minDays,
            maxDays,
            locationName: destination.locationName,
            regionArea: destination.regionArea,
            countryId: destination.countryId, // Slanje `countryId` umesto `countryName` i `continent`
            sort: `${sortField},${sortOrder}`,
            size: pageSize
        };

        if (priceType === 'total') {
            filters.minCost = minCost;
            filters.maxCost = maxCost;
        } else if (priceType === 'perPerson') {
            filters.minCostPerPerson = minCost;
            filters.maxCostPerPerson = maxCost;
        }

        const filteredFilters = Object.keys(filters)
            .filter(key => filters[key] !== '' && filters[key] !== null)
            .reduce((obj, key) => {
                obj[key] = filters[key];
                return obj;
            }, {});

        onFilterChange(filteredFilters);
    };

    const handleCountryChange = (e) => {
        setDestination(prevState => ({
            ...prevState,
            countryId: e.target.value // Smeštamo `countryId` u stanje
        }));
    };

    const handleSortFieldChange = (e) => {
        const selectedValue = e.target.value;
        if (selectedValue === 'cost') {
            setSortField(priceType === 'total' ? 'costs.totalCost' : 'costPerPerson');
        } else {
            setSortField(selectedValue);
        }
    };

    return (
        <div className="filters-container">
            <div className="filters-row">
                <label>Cena:</label>
                <input 
                    type="number" 
                    placeholder="Min" 
                    value={minCost} 
                    onChange={(e) => setMinCost(e.target.value)} 
                />
                <input 
                    type="number" 
                    placeholder="Max" 
                    value={maxCost} 
                    onChange={(e) => setMaxCost(e.target.value)} 
                />
                <select value={priceType} onChange={(e) => setPriceType(e.target.value)}>
                    <option value="total">Ukupno</option>
                    <option value="perPerson">Po osobi</option>
                </select>
            </div>
            <div className="filters-row">
                <label>Broj dana:</label>
                <input 
                    type="number" 
                    placeholder="Min" 
                    value={minDays} 
                    onChange={(e) => setMinDays(e.target.value)} 
                />
                <input 
                    type="number" 
                    placeholder="Max" 
                    value={maxDays} 
                    onChange={(e) => setMaxDays(e.target.value)} 
                />
            </div>
            <div className="filters-row">
                <label>Destinacija:</label>
                <input 
                    type="text" 
                    placeholder="Lokacija" 
                    value={destination.locationName} 
                    onChange={(e) => setDestination(prevState => ({
                        ...prevState,
                        locationName: e.target.value
                    }))} 
                />
                <input 
                    type="text" 
                    placeholder="Region" 
                    value={destination.regionArea} 
                    onChange={(e) => setDestination(prevState => ({
                        ...prevState,
                        regionArea: e.target.value
                    }))} 
                />
                <select value={destination.countryId} onChange={handleCountryChange}>
                    <option value="">Izaberite državu</option>
                    {countries.map(country => (
                        <option key={country.id} value={country.id}>
                            {country.countryName}
                        </option>
                    ))}
                </select>
            </div>
            <div className="filters-row">
                <label>Tip destinacije:</label>
                <select 
                    value={useCustomLocationType ? 'custom' : destination.locationType} 
                    onChange={(e) => {
                        const selectedValue = e.target.value;
                        if (selectedValue === 'custom') {
                            setUseCustomLocationType(true);
                            setDestination(prevState => ({
                                ...prevState,
                                locationType: ''
                            }));
                        } else {
                            setUseCustomLocationType(false);
                            setDestination(prevState => ({
                                ...prevState,
                                locationType: selectedValue
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
                    <option value="Jezero">Jezero</option>
                    <option value="custom">Prilagođeno</option>
                </select>

                {useCustomLocationType && (
                    <input 
                        type="text" 
                        placeholder="Unesite tip destinacije" 
                        value={customLocationType} 
                        onChange={(e) => {
                            const customValue = e.target.value;
                            setCustomLocationType(customValue);
                            setDestination(prevState => ({
                                ...prevState,
                                locationType: customValue
                            }));
                        }}
                    />
                )}
            </div>

            <div className="filters-row">
                <label>Sortiraj po:</label>
                <select value={sortField} onChange={handleSortFieldChange}>
                    <option value="cost">Cena</option>
                    <option value="createdAt">Datum objave</option>
                    <option value="daysSpent">Broj dana</option>
                </select>
                <select value={sortOrder} onChange={(e) => setSortOrder(e.target.value)}>
                    <option value="asc">Rastuće</option>
                    <option value="desc">Opadajuće</option>
                </select>
            </div>
            <div className="filters-row">
                <label>Broj putovanja po strani:</label>
                <input 
                    type="number" 
                    value={pageSize} 
                    onChange={(e) => setPageSize(e.target.value)} 
                    min="1"
                />
            </div>
            <div className="filters-row">
                <button onClick={handleApplyFilter}>Primeni odabrane filtere</button>
            </div>
        </div>
    );
};

export default Filters;
