import React, { useState, useEffect, useContext, useCallback } from 'react';
import api from '../services/api';
import Filters from './Filters';
import './ExperienceList.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit, faTrash, faThumbsUp, faComment } from '@fortawesome/free-solid-svg-icons';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContextProvider';
import defaultPhoto from '../assets/images/default_photo_1.png';

const ExperienceList = ({ currentUser }) => {
    const [experiences, setExperiences] = useState([]);
    const [filters, setFilters] = useState({});
    const [page, setPage] = useState(0); // Trenutna stranica
    const [totalPages, setTotalPages] = useState(1); // Ukupan broj stranica (ako je dostupan iz backend odgovora)

    const { userId: loggedInUserId } = useContext(AuthContext);

    const navigate = useNavigate();

    useEffect(() => {
        fetchExperiences();
    }, [filters, page, currentUser]);

    const fetchExperiences = async () => {
        try {
            const params = { ...filters, page };
            if (currentUser) {
                params.userId = currentUser.id; // Filtriramo po ID-ju korisnika ako je definisan
            }
            const response = await api.get('/experiences/search', { params });
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

    const handleEdit = (experienceId) => {
        navigate(`/experiences/edit/${experienceId}`);
    };

    const handleDelete = async (experienceId) => {
        try {
            const token = localStorage.getItem('accessToken'); // Dohvatanje tokena iz localStorage
            const config = {
                headers: { Authorization: `Bearer ${token}` } // Postavljanje tokena u zaglavlje
            };
            
            await api.delete(`experiences/${experienceId}`, config);
            setExperiences(prevExperiences => prevExperiences.filter(exp => exp.id !== experienceId));
        } catch (error) {
            console.error('Failed to delete experience:', error);
        }
    };

    const handleLike = useCallback(async (experienceId) => {
        const updatedExperiences = experiences.map(exp => {
            if (exp.id === experienceId) {
                const alreadyLiked = exp.likedByUsers.includes(loggedInUserId);
                const updatedLikes = alreadyLiked ? exp.likes - 1 : exp.likes + 1;
    
                return { 
                    ...exp, 
                    likes: updatedLikes,
                    likedByUsers: alreadyLiked
                        ? exp.likedByUsers.filter(userId => userId !== loggedInUserId)
                        : [...exp.likedByUsers, loggedInUserId]
                };
            }
            return exp;
        });
    
        setExperiences(updatedExperiences);
    
        try {
            const token = localStorage.getItem('accessToken');
            const config = {
                headers: { Authorization: `Bearer ${token}` }
            };
            await api.post(`/experiences/${experienceId}/like`, null, config);
        } catch (error) {
            console.error('Failed to like experience:', error);
            setExperiences(experiences); // Vraćanje na prethodno stanje
        }
    }, [experiences, loggedInUserId]);

    

    return (
        <div>
            <Filters onFilterChange={handleFilterChange} />
            <div>
                
<ul className="experience-list">
    {experiences.map(experience => (
        <li key={experience.id} className="experience-item">
            <div className="experience-image-container">
                <Link to={`/experiences/${experience.id}`}>
                    <img 
                        src={experience.images?.[0]
                            ? experience.images[0].startsWith('blob:')
                                ? experience.images[0] // Ako je Blob URL, koristi ga direktno
                                : `http://localhost:8080/uploads/${experience.images[0].split('\\').pop()}` // Ako nije, dodaj prefiks za server
                            : defaultPhoto} // Podrazumevana slika ako nema nijedne
                        alt="Experience" 
                        className="experience-image"
                    />
                </Link>
            </div>
            <div className="experience-main-content">
                <div className="experience-header">
                    <div className="experience-header-container-username">
                        <Link to={`/users/${experience.appUser?.id}`}>
                            <strong>{experience.appUser?.username || 'Nepoznat autor'}</strong>
                        </Link>
                    </div>
                    <div className="experience-header-container-cost">
                        <strong>Ukupni troškovi:</strong>
                    </div>
                    <div className="experience-header-container-description">
                        <strong>Opis putovanja:</strong>
                    </div>
                    <div className="experience-header-container">
                        <span>{experience.createdAt ? new Date(experience.createdAt).toLocaleDateString() : ''}</span>
                    </div>
                </div>
                <div className="experience-body">
                    <div className="experience-details">
                        <Link to={`/experiences/${experience.id}`} className="experience-title">
                            <h3>
                                {experience.destination?.locationName || ''}, {experience.daysSpent || 0} dana
                            </h3>
                        </Link>
                        <p className="experience-destination">
                            <strong>Destinacija:</strong> {experience.destination?.locationName || ''} 
                            {experience.destination?.regionArea && `, ${experience.destination.regionArea}`} 
                            {experience.destination?.country?.countryName && `, ${experience.destination.country.countryName}`} 
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
                        <div className="social-actions">
                            <button className="like-button">
                                <FontAwesomeIcon icon={faThumbsUp} className="action-icon" onClick={() => handleLike(experience.id)} />
                                {experience.likes}
                            </button>
                            <button className="comment-button" onClick={() => navigate(`/experiences/${experience.id}`)}>
    <FontAwesomeIcon icon={faComment} className="action-icon" />
    {experience.comments?.length || 0}
</button>
                        </div>
                        {experience.appUser && experience.appUser?.id === loggedInUserId && (  // Provera da li je trenutni korisnik vlasnik iskustva
                            <div className="owner-actions">
                                <button className="edit-button" onClick={() => handleEdit(experience.id)}>
                                    <FontAwesomeIcon icon={faEdit} className="action-icon" />
                                    Izmeni
                                </button>
                                <button className="delete-button" onClick={() => handleDelete(experience.id)}>
                                    <FontAwesomeIcon icon={faTrash} className="action-icon" />
                                    Izbriši
                                </button>
                            </div>
                        )}
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
