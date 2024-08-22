package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import com.jovandjumic.isap_travel_experiences_app.repositories.ExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.jovandjumic.isap_travel_experiences_app.utils.TestUtils.setId;

public class ExperienceServiceTest {

    @Mock
    private ExperienceRepository experienceRepository;

    @InjectMocks
    private ExperienceService experienceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExperience() {
        Experience experience = new Experience();
        experience.setDaysSpent(5);

        when(experienceRepository.save(experience)).thenReturn(experience);

        Experience createdExperience = experienceService.createExperience(experience);

        assertNotNull(createdExperience);
        assertEquals(5, createdExperience.getDaysSpent());
        verify(experienceRepository, times(1)).save(experience);
    }

    @Test
    void testGetExperienceById() throws Exception {
        Experience experience = new Experience();
        setId(experience,1L);
        experience.setDaysSpent(5);

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));

        Optional<Experience> foundExperience = experienceService.getExperienceById(1L);

        assertTrue(foundExperience.isPresent());
        assertEquals(5, foundExperience.get().getDaysSpent());
        verify(experienceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetExperienceByIdNotFound() {
        when(experienceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Experience> foundExperience = experienceService.getExperienceById(1L);

        assertFalse(foundExperience.isPresent());
        verify(experienceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllExperiences() throws Exception {
        Experience experience1 = new Experience();
        setId(experience1,1L);
        experience1.setDaysSpent(5);

        Experience experience2 = new Experience();
        setId(experience2,2L);
        experience2.setDaysSpent(7);

        when(experienceRepository.findAll()).thenReturn(Arrays.asList(experience1, experience2));

        List<Experience> allExperiences = experienceService.getAllExperiences();

        assertNotNull(allExperiences);
        assertEquals(2, allExperiences.size());
        verify(experienceRepository, times(1)).findAll();
    }

    @Test
    void testUpdateExperience() throws Exception {
        Experience experience = new Experience();
        setId(experience, 1L);
        experience.setDaysSpent(5);

        Experience updatedExperience = new Experience();
        updatedExperience.setDaysSpent(7);

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(experienceRepository.save(experience)).thenReturn(experience);

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNotNull(result);
        assertEquals(7, result.getDaysSpent());
        verify(experienceRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).save(experience);
    }

    @Test
    void testUpdateExperienceNotFound() {
        Experience updatedExperience = new Experience();
        updatedExperience.setDaysSpent(7);

        when(experienceRepository.findById(1L)).thenReturn(Optional.empty());

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNull(result);
        verify(experienceRepository, times(1)).findById(1L);
        verify(experienceRepository, times(0)).save(updatedExperience);
    }

    @Test
    void testDeleteExperience() {
        doNothing().when(experienceRepository).deleteById(1L);

        experienceService.deleteExperience(1L);

        verify(experienceRepository, times(1)).deleteById(1L);
    }
}
