package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Country;
import com.jovandjumic.isap_travel_experiences_app.entities.Destination;
import com.jovandjumic.isap_travel_experiences_app.repositories.DestinationRepository;
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

public class DestinationServiceTest {

    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private DestinationService destinationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDestination() {
        Destination destination = new Destination();
        destination.setLocationName("Paris");

        when(destinationRepository.save(destination)).thenReturn(destination);

        Destination createdDestination = destinationService.createDestination(destination);

        assertNotNull(createdDestination);
        assertEquals("Paris", createdDestination.getLocationName());
        verify(destinationRepository, times(1)).save(destination);
    }

    @Test
    void testGetDestinationById() throws Exception {
        Destination destination = new Destination();
        setId(destination, 1L);
        destination.setLocationName("Paris");

        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));

        Optional<Destination> foundDestination = destinationService.getDestinationById(1L);

        assertTrue(foundDestination.isPresent());
        assertEquals("Paris", foundDestination.get().getLocationName());
        verify(destinationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDestinationByIdNotFound() {
        when(destinationRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Destination> foundDestination = destinationService.getDestinationById(1L);

        assertFalse(foundDestination.isPresent());
        verify(destinationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllDestinations() throws Exception {
        Destination destination1 = new Destination();
        setId(destination1, 1L);
        destination1.setLocationName("Paris");

        Destination destination2 = new Destination();
        setId(destination2, 2L);
        destination2.setLocationName("London");

        when(destinationRepository.findAll()).thenReturn(Arrays.asList(destination1, destination2));

        List<Destination> allDestinations = destinationService.getAllDestinations();

        assertNotNull(allDestinations);
        assertEquals(2, allDestinations.size());
        verify(destinationRepository, times(1)).findAll();
    }

    @Test
    void testUpdateDestinationAllFieldsUpdated() throws Exception {
        Destination destination = new Destination();
        setId(destination, 1L);
        destination.setLocationName("Paris");
        destination.setLocationType("City");
        destination.setRegionArea("Île-de-France");
        Country france = new Country();
        france.setCountryName("France");
        destination.setCountry(france);

        Destination updatedDestination = new Destination();
        updatedDestination.setLocationName("Berlin");
        updatedDestination.setLocationType("City-State");
        updatedDestination.setRegionArea("Brandenburg");
        Country germany = new Country();
        germany.setCountryName("Germany");
        updatedDestination.setCountry(germany);

        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));
        when(destinationRepository.save(destination)).thenReturn(destination);

        Destination result = destinationService.updateDestination(1L, updatedDestination);

        assertNotNull(result);
        assertEquals("Berlin", result.getLocationName());
        assertEquals("City-State", result.getLocationType());
        assertEquals("Brandenburg", result.getRegionArea());
        assertEquals("Germany", result.getCountry().getCountryName());

        verify(destinationRepository, times(1)).findById(1L);
        verify(destinationRepository, times(1)).save(destination);
    }

    @Test
    void testUpdateDestinationSomeFieldsUpdated() throws Exception {
        Destination destination = new Destination();
        setId(destination, 1L);
        destination.setLocationName("Paris");
        destination.setLocationType("City");
        destination.setRegionArea("Île-de-France");
        Country france = new Country();
        france.setCountryName("France");
        destination.setCountry(france);

        Destination updatedDestination = new Destination();
        updatedDestination.setLocationName("Berlin");
        updatedDestination.setLocationType("City-State");

        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));
        when(destinationRepository.save(destination)).thenReturn(destination);

        Destination result = destinationService.updateDestination(1L, updatedDestination);

        assertNotNull(result);
        assertEquals("Berlin", result.getLocationName());
        assertEquals("City-State", result.getLocationType());
        assertEquals("Île-de-France", result.getRegionArea());
        assertEquals("France", result.getCountry().getCountryName());

        verify(destinationRepository, times(1)).findById(1L);
        verify(destinationRepository, times(1)).save(destination);
    }

    @Test
    void testUpdateDestinationNoFieldsUpdated() throws Exception {
        Destination destination = new Destination();
        setId(destination, 1L);
        destination.setLocationName("Paris");
        destination.setLocationType("City");
        destination.setRegionArea("Île-de-France");
        Country france = new Country();
        france.setCountryName("France");
        destination.setCountry(france);

        Destination updatedDestination = new Destination();

        when(destinationRepository.findById(1L)).thenReturn(Optional.of(destination));
        when(destinationRepository.save(destination)).thenReturn(destination);

        Destination result = destinationService.updateDestination(1L, updatedDestination);

        assertNotNull(result);
        assertEquals("Paris", result.getLocationName());
        assertEquals("City", result.getLocationType());
        assertEquals("Île-de-France", result.getRegionArea());
        assertEquals("France", result.getCountry().getCountryName());

        verify(destinationRepository, times(1)).findById(1L);
        verify(destinationRepository, times(1)).save(destination);
    }

    @Test
    void testUpdateDestinationNotFound() {
        Destination updatedDestination = new Destination();
        updatedDestination.setLocationName("Berlin");

        when(destinationRepository.findById(1L)).thenReturn(Optional.empty());

        Destination result = destinationService.updateDestination(1L, updatedDestination);

        assertNull(result);
        verify(destinationRepository, times(1)).findById(1L);
        verify(destinationRepository, times(0)).save(updatedDestination);
    }

    @Test
    void testDeleteDestination() {
        doNothing().when(destinationRepository).deleteById(1L);

        destinationService.deleteDestination(1L);

        verify(destinationRepository, times(1)).deleteById(1L);
    }
}
