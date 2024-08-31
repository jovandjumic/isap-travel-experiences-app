import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import ExperienceList from './ExperienceList'; // Ponovno korišćenje liste iskustava
import './UserProfile.css';

const UserProfile = () => {
    const { id } = useParams(); // Dohvatamo ID iz URL parametara
    const [user, setUser] = useState(null);

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

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <div className="user-profile">
            <div className="user-header">
                <img src={user.profilePicture || '/path/to/default-avatar.jpg'} alt={`${user.username} avatar`} className="profile-picture" />
                <div className="user-info">
                    <h2>{user.firstName} {user.lastName}</h2> {/* Koristimo firstName i lastName umesto name i surname */}
                    <p>@{user.username}</p>
                    <p>Registrovan: {user.registrationDate ? new Date(user.registrationDate).toLocaleDateString() : 'Nepoznato'}</p>
                    <p className="user-bio">{user.biography || 'Korisnik nije dodao biografiju.'}</p>
                </div>
            </div>
            <h3>Iskustva korisnika {user.username}</h3>
            <ExperienceList currentUser={user} />
        </div>
    );
};

export default UserProfile;
