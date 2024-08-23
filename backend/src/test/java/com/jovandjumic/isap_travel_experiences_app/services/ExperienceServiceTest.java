package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Costs;
import com.jovandjumic.isap_travel_experiences_app.entities.Destination;
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
    void testUpdateExperienceAllFieldsUpdated() throws Exception {
        Experience experience = new Experience();
        setId(experience, 1L);
        Destination paris = new Destination();
        paris.setLocationName("Paris");
        experience.setDestination(paris);
        experience.setDaysSpent(5);
        Costs costs = new Costs();
        costs.setTravelCost(1000.0);
        costs.setAccommodationCost(500.0);
        costs.setOtherCosts(200.0);
        experience.setCosts(costs);
        experience.setRating(4.5);

        Experience updatedExperience = new Experience();
        Destination berlin = new Destination();
        berlin.setLocationName("Berlin");
        updatedExperience.setDestination(berlin);
        updatedExperience.setDaysSpent(7);
        Costs updatedCosts = new Costs();
        updatedCosts.setTravelCost(1200.0);
        updatedCosts.setAccommodationCost(600.0);
        updatedCosts.setOtherCosts(250.0);
        updatedExperience.setCosts(updatedCosts);
        updatedExperience.setRating(4.8);

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(experienceRepository.save(experience)).thenReturn(experience);

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNotNull(result);
        assertEquals("Berlin", result.getDestination().getLocationName());
        assertEquals(7, result.getDaysSpent());
        assertEquals(1200.0, result.getCosts().getTravelCost());
        assertEquals(600.0, result.getCosts().getAccommodationCost());
        assertEquals(250.0, result.getCosts().getOtherCosts());
        assertEquals(4.8, result.getRating());
        verify(experienceRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).save(experience);
    }

    @Test
    void testUpdateExperienceSomeFieldsUpdated() throws Exception {
        Experience experience = new Experience();
        setId(experience, 1L);
        Destination paris = new Destination();
        paris.setLocationName("Paris");
        experience.setDestination(paris);
        experience.setDaysSpent(5);
        Costs costs = new Costs();
        costs.setTravelCost(1000.0);
        costs.setAccommodationCost(500.0);
        costs.setOtherCosts(200.0);
        experience.setCosts(costs);
        experience.setRating(4.5);

        Experience updatedExperience = new Experience();
        Costs updatedCosts = new Costs();
        updatedCosts.setTravelCost(1200.0);
        updatedCosts.setAccommodationCost(600.0);
        updatedCosts.setOtherCosts(250.0);
        updatedExperience.setCosts(updatedCosts);
        updatedExperience.setRating(4.8);

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(experienceRepository.save(experience)).thenReturn(experience);

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNotNull(result);
        assertEquals("Paris", result.getDestination().getLocationName());
        assertEquals(5, result.getDaysSpent());
        assertEquals(1200.0, result.getCosts().getTravelCost());
        assertEquals(600.0, result.getCosts().getAccommodationCost());
        assertEquals(250.0, result.getCosts().getOtherCosts());
        assertEquals(4.8, result.getRating());
        verify(experienceRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).save(experience);
    }

    @Test
    void testUpdateExperienceNoFieldsUpdated() throws Exception {
        Experience experience = new Experience();
        setId(experience, 1L);
        Destination paris = new Destination();
        paris.setLocationName("Paris");
        experience.setDestination(paris);
        experience.setDaysSpent(5);
        Costs costs = new Costs();
        costs.setTravelCost(1000.0);
        costs.setAccommodationCost(500.0);
        costs.setOtherCosts(200.0);
        experience.setCosts(costs);
        experience.setRating(4.5);

        Experience updatedExperience = new Experience();

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(experienceRepository.save(experience)).thenReturn(experience);

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNotNull(result);
        assertEquals("Paris", result.getDestination().getLocationName());
        assertEquals(5, result.getDaysSpent());
        assertEquals(1000.0, result.getCosts().getTravelCost());
        assertEquals(500.0, result.getCosts().getAccommodationCost());
        assertEquals(200.0, result.getCosts().getOtherCosts());
        assertEquals(4.5, result.getRating());
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
