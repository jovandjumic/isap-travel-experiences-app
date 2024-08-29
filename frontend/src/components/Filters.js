import React, { useState } from 'react';
import './Filters.css';

const Filters = ({ onFilterChange }) => {
    const [daysMin, setDaysMin] = useState('');
    const [daysMax, setDaysMax] = useState('');
    const [priceMin, setPriceMin] = useState('');
    const [priceMax, setPriceMax] = useState('');
    const [priceType, setPriceType] = useState('total');
    const [location, setLocation] = useState('');
    const [region, setRegion] = useState('');
    const [country, setCountry] = useState('');
    const [continent, setContinent] = useState('');
    const [destinationType, setDestinationType] = useState('');
    const [sortBy, setSortBy] = useState('price');
    const [sortOrder, setSortOrder] = useState('asc');

    const handleApplyFilter = () => {
        const filters = {
            daysMin,
            daysMax,
            priceMin,
            priceMax,
            priceType,
            location,
            region,
            country,
            continent,
            destinationType,
            sortBy,
            sortOrder,
        };
        onFilterChange(filters);
    };

    return (
        <div className="filters-container">
            <div className="filters-row">
                <label>Cena:</label>
                <input 
                    type="number" 
                    placeholder="Min" 
                    value={priceMin} 
                    onChange={(e) => setPriceMin(e.target.value)} 
                />
                <input 
                    type="number" 
                    placeholder="Max" 
                    value={priceMax} 
                    onChange={(e) => setPriceMax(e.target.value)} 
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
                    value={daysMin} 
                    onChange={(e) => setDaysMin(e.target.value)} 
                />
                <input 
                    type="number" 
                    placeholder="Max" 
                    value={daysMax} 
                    onChange={(e) => setDaysMax(e.target.value)} 
                />
            </div>
            <div className="filters-row">
                <label>Destinacija:</label>
                <input 
                    type="text" 
                    placeholder="Lokacija" 
                    value={location} 
                    onChange={(e) => setLocation(e.target.value)} 
                />
                <input 
                    type="text" 
                    placeholder="Region" 
                    value={region} 
                    onChange={(e) => setRegion(e.target.value)} 
                />
                <input 
                    type="text" 
                    placeholder="Država" 
                    value={country} 
                    onChange={(e) => setCountry(e.target.value)} 
                />
                <input 
                    type="text" 
                    placeholder="Kontinent" 
                    value={continent} 
                    onChange={(e) => setContinent(e.target.value)} 
                />
            </div>
            <div className="filters-row">
                <label>Tip destinacije:</label>
                <input 
                    type="text" 
                    placeholder="Tip destinacije" 
                    value={destinationType} 
                    onChange={(e) => setDestinationType(e.target.value)} 
                />
            </div>
            <div className="filters-row">
                <label>Sortiraj po:</label>
                <select value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
                    <option value="price">Cena</option>
                    <option value="date">Datum objave</option>
                    <option value="days">Broj dana</option>
                </select>
                <select value={sortOrder} onChange={(e) => setSortOrder(e.target.value)}>
                    <option value="asc">Rastuće</option>
                    <option value="desc">Opadajuće</option>
                </select>
            </div>
            <div className="filters-row">
                <button onClick={handleApplyFilter}>Primeni odabrane filtere</button>
            </div>
        </div>
    );
};

export default Filters;
