import React, { useState, useEffect } from 'react';
import axios from 'axios';
import api from './services/api';

const ExperiencesPage = () => {
    const [experiences, setExperiences] = useState([]);

    useEffect(() => {
        fetchExperiences();
    }, []);

    const fetchExperiences = async () => {
        try {
            const response = await api.get('/experiences');
            setExperiences(response.data.content);
        } catch (error) {
            console.error("Error fetching experiences:", error);
        }
    };

    return (
        <div>
            <ul>
                {experiences.map(experience => (
                    <li key={experience.id}>
                        <h2>{experience.destination.locationName}</h2>
                        <p>Days Spent: {experience.daysSpent}</p>
                        <p>Cost Per Person: {experience.costPerPerson}</p>
                        <p>Likes: {experience.likes}</p>
                        <p>Comments: {experience.comments.length}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ExperiencesPage;