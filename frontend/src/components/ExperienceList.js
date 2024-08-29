import React, { useState, useEffect } from 'react';
import api from '../services/api';
import Filters from './Filters';
import './ExperienceList.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faThumbsUp, faComment } from '@fortawesome/free-solid-svg-icons';

const ExperienceList = () => {
    const [experiences, setExperiences] = useState([]);
    const [filters, setFilters] = useState({});
    const [page, setPage] = useState(0); // Trenutna stranica
    const [totalPages, setTotalPages] = useState(1); // Ukupan broj stranica (ako je dostupan iz backend odgovora)

    useEffect(() => {
        fetchExperiences();
    }, [filters, page]);

    const fetchExperiences = async () => {
        try {
            const response = await api.get('/experiences/search', { 
                params: { ...filters, page } // Prosleđujemo trenutnu stranicu kao parametar
            });
            setExperiences(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error("Error fetching experiences:", error);
        }
    };

    const handleFilterChange = (newFilters) => {
        setFilters(newFilters);
        setPage(0); // Resetujemo na prvu stranicu prilikom promene filtera
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setPage(newPage);
        }
    };

    const truncateDescription = (description, maxLength) => {
        const defaultDescription = "Ovo je podrazumevani opis iskustva jer opis nije dostupan.";
        const desc = description || defaultDescription;
        if (desc.length > maxLength) {
            return desc.slice(0, maxLength) + '...';
        }
        return desc;
    };

    return (
        <div>
            <Filters onFilterChange={handleFilterChange} />
            <div>
                <ul className="experience-list">
                    {experiences.map(experience => (
                        <li key={experience.id} className="experience-item">
                            <div className="experience-image-container">
                                <img 
                                    src={experience.images?.[0] || '/path/to/default-image.jpg'} 
                                    alt="Experience" 
                                    className="experience-image" 
                                />
                            </div>
                            <div className="experience-main-content">
                                <div className="experience-header">
                                    <span><strong>{experience.appUser?.username || 'Nepoznat autor'}</strong></span>
                                    <span>{experience.createdAt ? new Date(experience.createdAt).toLocaleDateString() : ''}</span>
                                </div>
                                <div className="experience-body">
                                    <div className="experience-details">
                                        <h3 className="experience-title">
                                            {experience.destination?.locationName || ''}, {experience.daysSpent || 0} dana
                                        </h3>
                                        <p className="experience-destination">
                                            <strong>Destinacija:</strong> {experience.destination?.locationName || ''} 
                                            {experience.destination?.regionArea && `, ${experience.destination.regionArea}`} 
                                            {experience.destination?.country?.countryName && `, ${experience.destination.country.countryName}`} 
                                            {experience.destination?.country?.continent && `, ${experience.destination.country.continent}`}
                                        </p>
                                        {experience.destination?.locationType && (
                                            <p className="experience-location-type">
                                                <strong>Tip destinacije:</strong> {experience.destination.locationType}
                                            </p>
                                        )}
                                        <p className="experience-people">
                                            <strong>Broj osoba:</strong> {experience.numberOfPeople || 'Nije navedeno'}
                                        </p>
                                    </div>
                                    <div className="experience-meta">
                                        <p className="experience-cost">
                                            {experience.costs?.totalCost || 0} €
                                        </p>
                                    </div>
                                    <div className="experience-description-container">
                                        <p className="experience-description">
                                            {truncateDescription(experience.description, 150)}
                                        </p>
                                    </div>
                                    <div className="experience-actions-container">
                                        <button className="like-button">
                                            <FontAwesomeIcon icon={faThumbsUp} className="action-icon" />
                                            ({experience.likes || 0})
                                        </button>
                                        <button className="comment-button">
                                            <FontAwesomeIcon icon={faComment} className="action-icon" />
                                            {experience.comments?.length || 0}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
                <div className="pagination">
                    <button onClick={() => handlePageChange(page - 1)} disabled={page === 0}>Prethodna</button>
                    <span>Stranica {page + 1} od {totalPages}</span>
                    <button onClick={() => handlePageChange(page + 1)} disabled={page === totalPages - 1}>Sledeća</button>
                </div>
            </div>
        </div>
    );
};

export default ExperienceList;
