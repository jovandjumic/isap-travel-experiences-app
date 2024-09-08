import React, { useState, useEffect, useContext } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import api from '../services/api';
import './ExperienceDetails.css';
import defaultPhoto from '../assets/images/default_photo_1.png';
import { AuthContext } from '../contexts/AuthContextProvider';
import { faThumbsUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const ExperienceDetails = () => {
    const { id } = useParams();
    const { userId: loggedInUserId } = useContext(AuthContext);
    const [experience, setExperience] = useState(null);
    const [countries, setCountries] = useState([]); // State za čuvanje država
    const [newComment, setNewComment] = useState('');
    const [currentImageIndex, setCurrentImageIndex] = useState(0);
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [updatedComment, setUpdatedComment] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        const fetchExperience = async () => {
            try {
                const response = await api.get(`/experiences/${id}`);
                setExperience(response.data);
            } catch (error) {
                console.error('Failed to fetch experience details:', error);
            }
        };
        fetchExperience();
    }, [id]);

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

    const handleLike = async () => {
        try {
            const token = localStorage.getItem('accessToken');
            const config = {
                headers: { Authorization: `Bearer ${token}` },
            };
            const updatedLikes = experience.likedByUsers.includes(loggedInUserId)
                ? experience.likes - 1
                : experience.likes + 1;
    
            const updatedLikedByUsers = experience.likedByUsers.includes(loggedInUserId)
                ? experience.likedByUsers.filter((userId) => userId !== loggedInUserId)
                : [...experience.likedByUsers, loggedInUserId];
    
            setExperience({
                ...experience,
                likes: updatedLikes,
                likedByUsers: updatedLikedByUsers,
            });
    
            await api.post(`/experiences/${id}/like`, null, config);
        } catch (error) {
            console.error('Failed to like experience:', error);
        }
    };

    const handleDeleteExperience = async (experienceId) => {
        try {
            const token = localStorage.getItem('accessToken'); // Token iz localStorage
            const config = {
                headers: { Authorization: `Bearer ${token}` }
            };
            await api.delete(`/experiences/${experienceId}`, config);
            navigate('/experiences'); // Nakon brisanja preusmerava korisnika na listu iskustava
        } catch (error) {
            console.error('Failed to delete experience:', error);
        }
    };

    const handleNextImage = () => {
        setCurrentImageIndex((prevIndex) => (prevIndex + 1) % experience.images.length);
    };

    const handlePrevImage = () => {
        setCurrentImageIndex((prevIndex) =>
            prevIndex === 0 ? experience.images.length - 1 : prevIndex - 1
        );
    };

    const handleAddComment = async () => {
        try {
            const token = localStorage.getItem('accessToken'); // Dohvatanje tokena iz localStorage
            const config = {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            };
            const response = await api.post(`/comments/${id}`, { content: newComment }, config);
            setExperience({
                ...experience,
                comments: [...experience.comments, response.data],
            });
            setNewComment('');
        } catch (error) {
            console.error('Failed to add comment:', error);
        }
    };

    const handleDeleteComment = async (commentId) => {
        try {
            const token = localStorage.getItem('accessToken');
            const config = {
                headers: { Authorization: `Bearer ${token}` },
            };
            await api.delete(`/comments/${commentId}`, config);
            setExperience({
                ...experience,
                comments: experience.comments.filter((comment) => comment.id !== commentId),
            });
        } catch (error) {
            console.error('Failed to delete comment:', error);
        }
    };

    const handleUpdateComment = async (commentId) => {
        try {
            const token = localStorage.getItem('accessToken');
            const config = {
                headers: { Authorization: `Bearer ${token}` },
            };
            const response = await api.put(`/comments/${commentId}`, { content: updatedComment }, config);
            setExperience({
                ...experience,
                comments: experience.comments.map((comment) =>
                    comment.id === commentId ? response.data : comment
                ),
            });
            setEditingCommentId(null); // Resetovanje nakon što se komentar ažurira
        } catch (error) {
            console.error('Failed to update comment:', error);
        }
    };

    if (!experience) {
        return <div>Loading...</div>;
    }

    const imageUrl = experience.images?.[currentImageIndex]
        ? `${process.env.REACT_APP_IMAGE_URL}/uploads/${experience.images[currentImageIndex].split('/').pop()}`
        : defaultPhoto;

    const country = countries.find(c => c.id === experience.destination?.countryId); // Pronalazimo državu po ID-ju

    return (
        <div>
            <div className="experience-details-header-container">
                <h1 className="experience-details-title">
                    {experience.destination?.locationName}, {experience.daysSpent} dana
                </h1>

                <button className="details-like-button" onClick={handleLike}>
                    <FontAwesomeIcon icon={faThumbsUp} className="action-icon" />
                    {experience.likes}
                </button>

                {experience.appUser?.id === loggedInUserId && (
                    <div className="experience-details-actions">
                        <button className="details-edit-button" onClick={() => navigate(`/experiences/edit/${id}`)}>
                            Izmeni
                        </button>
                        <button className="details-delete-button" onClick={() => handleDeleteExperience(id)}>
                            Izbriši
                        </button>
                    </div>
                )}
            </div>

            <div className="experience-details-main">
                <div className="experience-details-info">
                    <div className="experience-detail">
                        <strong>Destinacija:</strong> {experience.destination?.locationName || ''} 
                        {experience.destination?.regionArea ? `, ${experience.destination.regionArea}` : ''} 
                        {country ? `, ${country.countryName}` : ''} {/* Prikaz države */}
                    </div>
                    <div className="experience-detail">
                        <strong>Tip destinacije:</strong> {experience.destination?.locationType || 'Nepoznato'}
                    </div>
                    <div className="experience-detail">
                        <strong>Broj osoba:</strong> {experience.numberOfPeople || 'Nepoznato'}
                    </div>
                    <div className="experience-detail">
                        <strong>Prevozno sredstvo:</strong> {experience.costs?.travelMode || 'Nepoznato'}
                    </div>
                    <div className="experience-detail">
                        <strong>Putna ruta:</strong> {experience.costs?.travelRoute || 'Nepoznato'}
                    </div>
                </div>

                <div className="experience-details-costs">
                    <div className="cost-detail">
                        <strong>Troškovi prevoza:</strong> {experience.costs?.travelCost || 0} €
                    </div>
                    <div className="cost-detail">
                        <strong>Troškovi smeštaja:</strong> {experience.costs?.accommodationCost || 0} €
                    </div>
                    <div className="cost-detail">
                        <strong>Ostali troškovi:</strong> {experience.costs?.otherCosts || 0} €
                    </div>
                    <hr className="cost-divider" />
                    <div className="cost-detail">
                        <strong>Ukupni troškovi: </strong>{experience.costs?.totalCost || 0} €
                    </div>
                </div>
                <div className="experience-details-images">
                    <div className="image-wrapper">
                        <img src={imageUrl} alt="Experience" className="experience-details-image" />
                        {experience.images?.length > 1 && (
                            <div className="image-navigation">
                                <button className="prev-image" onClick={handlePrevImage}>
                                    &lt; Prethodna
                                </button>
                                <span className="image-counter">
                                    {currentImageIndex + 1} od {experience.images.length}
                                </span>
                                <button className="next-image" onClick={handleNextImage}>
                                    Sledeća &gt;
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>

            <div className="experience-details-description">
                <h2>Opis putovanja</h2>
                <p>{experience.description || 'Opis nije dostupan.'}</p>
            </div>

            <div className="experience-details-comments">
                <h2>Komentari</h2>

                <div className="add-comment-section">
                    <textarea
                        className="add-comment-input"
                        value={newComment}
                        onChange={(e) => setNewComment(e.target.value)}
                        placeholder="Dodajte svoj komentar..."
                    />
                    <button className="add-comment-button" onClick={handleAddComment}>
                        Dodaj
                    </button>
                </div>

                {experience.comments.length > 0 ? (
                    experience.comments.map((comment) => (
                        <div key={comment.id} className="comment-item">
                            <div className="comment-header">
                                <Link to={`/users/${comment.appUser?.id}`}>
                                    <strong>{comment.appUser?.username}</strong>
                                </Link>
                                <span className="comment-date">{new Date(comment.commentDate).toLocaleDateString()}</span>
                            </div>

                            {editingCommentId === comment.id ? (
                                <div
                                    className="comment-content editable"
                                    contentEditable={true}
                                    suppressContentEditableWarning={true}
                                    onBlur={(e) => setUpdatedComment(e.target.textContent)}
                                >
                                    {comment.content}
                                </div>
                            ) : (
                                <div className="comment-content">{comment.content}</div>
                            )}

                            {comment.appUser?.id === loggedInUserId && (
                                <div className="comment-actions">
                                    {editingCommentId === comment.id ? (
                                        <button
                                            className="add-comment-button"
                                            onClick={() => handleUpdateComment(comment.id)}
                                        >
                                            Sačuvaj
                                        </button>
                                    ) : (
                                        <button
                                            className="add-comment-button"
                                            onClick={() => {
                                                setEditingCommentId(comment.id);
                                                setUpdatedComment(comment.content);
                                            }}
                                        >
                                            Izmeni
                                        </button>
                                    )}
                                    <button
                                        className="add-comment-button"
                                        onClick={() => handleDeleteComment(comment.id)}
                                    >
                                        Izbriši
                                    </button>
                                </div>
                            )}
                        </div>
                    ))
                ) : (
                    <p>Nema komentara.</p>
                )}
            </div>
        </div>
    );
};

export default ExperienceDetails;
