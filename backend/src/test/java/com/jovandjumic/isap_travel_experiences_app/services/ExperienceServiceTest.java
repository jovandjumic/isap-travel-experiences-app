package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.*;
import com.jovandjumic.isap_travel_experiences_app.repositories.CountryRepository;
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

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExperience() throws Exception {
        // Mock AppUser
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        setId(appUser, 1L);

        // Kreiraj Country i Destination
        Country country = new Country();
        country.setId(123L);  // Postavi ID države
        when(countryRepository.findById(123L)).thenReturn(Optional.of(country));  // Mockovanje countryRepository-ja

        Destination destination = new Destination();
        destination.setLocationName("Paris");
        destination.setCountryId(123L);  // Postavi countryId

        // Kreiraj Costs objekat i postavi vrednosti
        Costs costs = new Costs();
        costs.setTravelCost(100.0);
        costs.setAccommodationCost(200.0);
        costs.setOtherCosts(50.0);

        // Kreiraj Experience i postavi AppUser-a, Destination, Costs, i broj osoba
        Experience experience = new Experience();
        experience.setDaysSpent(5);
        experience.setAppUser(appUser);
        experience.setDestination(destination);
        experience.setCosts(costs);
        experience.setNumberOfPeople(4);  // Postavi broj osoba pre kreiranja Experience-a

        // Mock metoda za current user
        when(userService.getCurrentUser()).thenReturn(appUser);

        // Mock save metode u repozitorijumu
        when(experienceRepository.save(experience)).thenReturn(experience);

        // Poziv metode
        Experience createdExperience = experienceService.createExperience(experience);

        // Provere
        assertNotNull(createdExperience);
        assertEquals(5, createdExperience.getDaysSpent());
        assertEquals("Paris", createdExperience.getDestination().getLocationName());
        assertEquals(123L, createdExperience.getDestination().getCountryId());
        assertEquals(100.0, createdExperience.getCosts().getTravelCost());
        assertEquals(4, createdExperience.getNumberOfPeople());  // Provera postavljenog broja osoba
        verify(experienceRepository, times(1)).save(experience);
    }

    @Test
    void testGetExperienceByIdNotFound() {
        when(experienceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Experience> foundExperience = experienceService.getExperienceById(1L);

        assertFalse(foundExperience.isPresent());
        verify(experienceRepository, times(1)).findById(1L);
    }


    @Test
    void testUpdateExperienceNotFound() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        setId(appUser, 1L);

        Experience updatedExperience = new Experience();
        updatedExperience.setDaysSpent(7);

        when(userService.getCurrentUser()).thenReturn(appUser);
        when(experienceRepository.findById(1L)).thenReturn(Optional.empty());

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNull(result);
        verify(experienceRepository, times(1)).findById(1L);
        verify(experienceRepository, times(0)).save(updatedExperience);
    }

    @Test
    void testGetExperienceById() throws Exception {
        // Kreiraj Experience sa Destination, Costs i AppUser
        Experience experience = new Experience();
        setId(experience, 1L);
        experience.setDaysSpent(5);

        // Postavi Costs i broj osoba
        Costs costs = new Costs();
        costs.setTravelCost(100.0);
        experience.setCosts(costs);
        experience.setNumberOfPeople(4);  // Postavi broj osoba

        Destination destination = new Destination();
        destination.setLocationName("Paris");
        experience.setDestination(destination);

        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));

        Optional<Experience> foundExperience = experienceService.getExperienceById(1L);

        assertTrue(foundExperience.isPresent());
        assertEquals(5, foundExperience.get().getDaysSpent());
        verify(experienceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllExperiences() throws Exception {
        // Kreiraj dva Experience-a sa Destination, Costs i AppUser
        Experience experience1 = new Experience();
        setId(experience1, 1L);
        experience1.setDaysSpent(5);
        experience1.setNumberOfPeople(4);  // Postavi broj osoba
        experience1.setCosts(new Costs());

        Experience experience2 = new Experience();
        setId(experience2, 2L);
        experience2.setDaysSpent(7);
        experience2.setNumberOfPeople(3);  // Postavi broj osoba
        experience2.setCosts(new Costs());

        when(experienceRepository.findAll()).thenReturn(Arrays.asList(experience1, experience2));

        List<Experience> allExperiences = experienceService.getAllExperiences();

        assertNotNull(allExperiences);
        assertEquals(2, allExperiences.size());
        verify(experienceRepository, times(1)).findAll();
    }

    @Test
    void testUpdateExperienceAllFieldsUpdated() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        setId(appUser, 1L);
        // Postavljanje početnog Experience-a
        Experience experience = new Experience();
        setId(experience, 1L);
        experience.setDaysSpent(5);
        experience.setNumberOfPeople(4);  // Postavi broj osoba
        Costs costs = new Costs();
        costs.setTravelCost(1000.0);
        costs.setAccommodationCost(500.0);
        costs.setOtherCosts(200.0);
        experience.setCosts(costs);
        experience.setAppUser(appUser);

        Destination paris = new Destination();
        paris.setLocationName("Paris");
        experience.setDestination(paris);

        // Kreiraj ažurirani Experience
        Experience updatedExperience = new Experience();
        updatedExperience.setDaysSpent(7);
        updatedExperience.setNumberOfPeople(3);  // Postavi broj osoba
        Costs updatedCosts = new Costs();
        updatedCosts.setTravelCost(1200.0);
        updatedCosts.setAccommodationCost(600.0);
        updatedCosts.setOtherCosts(250.0);
        updatedExperience.setCosts(updatedCosts);

        Destination berlin = new Destination();
        berlin.setLocationName("Berlin");
        updatedExperience.setDestination(berlin);

        when(userService.getCurrentUser()).thenReturn(appUser);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(experienceRepository.save(experience)).thenReturn(experience);

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNotNull(result);
        assertEquals("Berlin", result.getDestination().getLocationName());
        assertEquals(7, result.getDaysSpent());
        assertEquals(1200.0, result.getCosts().getTravelCost());
        assertEquals(600.0, result.getCosts().getAccommodationCost());
        assertEquals(250.0, result.getCosts().getOtherCosts());
        verify(experienceRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).save(experience);
    }

    @Test
    void testUpdateExperienceSomeFieldsUpdated() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        setId(appUser, 1L);
        // Postavljanje početnog Experience-a
        Experience experience = new Experience();
        setId(experience, 1L);
        experience.setDaysSpent(5);
        experience.setNumberOfPeople(4);  // Postavi broj osoba
        Costs costs = new Costs();
        costs.setTravelCost(1000.0);
        costs.setAccommodationCost(500.0);
        costs.setOtherCosts(200.0);
        experience.setCosts(costs);
        experience.setAppUser(appUser);

        Destination paris = new Destination();
        paris.setLocationName("Paris");
        experience.setDestination(paris);

        // Kreiraj ažurirani Experience sa samo ažuriranim troškovima
        Experience updatedExperience = new Experience();
        Costs updatedCosts = new Costs();
        updatedCosts.setTravelCost(1200.0);
        updatedCosts.setAccommodationCost(600.0);
        updatedCosts.setOtherCosts(250.0);
        updatedExperience.setCosts(updatedCosts);

        when(userService.getCurrentUser()).thenReturn(appUser);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(experienceRepository.save(experience)).thenReturn(experience);

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNotNull(result);
        assertEquals("Paris", result.getDestination().getLocationName());
        assertEquals(5, result.getDaysSpent());
        assertEquals(1200.0, result.getCosts().getTravelCost());
        assertEquals(600.0, result.getCosts().getAccommodationCost());
        assertEquals(250.0, result.getCosts().getOtherCosts());
        verify(experienceRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).save(experience);
    }

    @Test
    void testUpdateExperienceNoFieldsUpdated() throws Exception {
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        setId(appUser, 1L);
        // Postavljanje početnog Experience-a
        Experience experience = new Experience();
        setId(experience, 1L);
        experience.setDaysSpent(5);
        experience.setNumberOfPeople(4);  // Postavi broj osoba
        Costs costs = new Costs();
        costs.setTravelCost(1000.0);
        costs.setAccommodationCost(500.0);
        costs.setOtherCosts(200.0);
        experience.setCosts(costs);
        experience.setAppUser(appUser);

        Destination paris = new Destination();
        paris.setLocationName("Paris");
        experience.setDestination(paris);

        // Kreiraj prazan ažurirani Experience
        Experience updatedExperience = new Experience();

        when(userService.getCurrentUser()).thenReturn(appUser);
        when(experienceRepository.findById(1L)).thenReturn(Optional.of(experience));
        when(experienceRepository.save(experience)).thenReturn(experience);

        Experience result = experienceService.updateExperience(1L, updatedExperience);

        assertNotNull(result);
        assertEquals("Paris", result.getDestination().getLocationName());
        assertEquals(5, result.getDaysSpent());
        assertEquals(1000.0, result.getCosts().getTravelCost());
        assertEquals(500.0, result.getCosts().getAccommodationCost());
        assertEquals(200.0, result.getCosts().getOtherCosts());
        verify(experienceRepository, times(1)).findById(1L);
        verify(experienceRepository, times(1)).save(experience);
    }

}
