import React, { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';
import ExperienceList from './ExperienceList';
import './UserProfile.css';
import { AuthContext } from '../contexts/AuthContextProvider';

const UserProfile = () => {
    const { id } = useParams();
    const [user, setUser] = useState(null);
    const { userId: loggedInUserId } = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        fetchUserProfile();
    }, [id]);

    const fetchUserProfile = async () => {
        try {
            const response = await api.get(`/users/${id}`);
            setUser(response.data);
        } catch (error) {
            console.error('Error fetching user profile:', error);
        }
    };

    const handleEditProfile = () => {
        navigate(`/users/${id}/edit`);
    };

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <div className="user-profile">
            <div className="user-header">
                <p className="registration-date">
                    Registrovan: {user.registrationDate ? new Date(user.registrationDate).toLocaleDateString() : 'Nepoznato'}
                </p>
                <div className="user-info-container">
                    <div className="profile-picture-container">
                    <img 
    src={user.profilePicture ? `http://localhost:8080/uploads/${user.profilePicture.split('\\').pop()}` : '/path/to/default-avatar.jpg'} 
    alt={`${user.username} avatar`} 
    className="profile-picture" 
/>

                    </div>
                    <div className="user-info-bio-container">
                        <div className="name-username-container">
                            <h2>{user.firstName} {user.lastName}</h2>
                            <h3>@{user.username}</h3>
                        </div>
                        <div className="bio-container">
                            <h4>Biografija:</h4>
                            <p className="user-bio">{user.biography || 'Korisnik nije dodao biografiju.'}</p>
                        </div>
                    </div>
                </div>
                {loggedInUserId === user.id && (
                    <div className="edit-container">
                        <button className="edit-profile-button" onClick={handleEditProfile}>
                            Izmeni korisničke podatke
                        </button>
                    </div>
                )}
            </div>
            <h3>Iskustva korisnika {user.username}</h3>
            <ExperienceList currentUser={user} />
        </div>
    );
};

export default UserProfile;
