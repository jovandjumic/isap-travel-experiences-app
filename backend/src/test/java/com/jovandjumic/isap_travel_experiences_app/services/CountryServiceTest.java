package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Country;
import com.jovandjumic.isap_travel_experiences_app.repositories.CountryRepository;
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

public class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCountry() {
        Country country = new Country();
        country.setCountryName("France");

        when(countryRepository.save(country)).thenReturn(country);

        Country createdCountry = countryService.createCountry(country);

        assertNotNull(createdCountry);
        assertEquals("France", createdCountry.getCountryName());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    void testGetCountryById() throws Exception {
        Country country = new Country();
        setId(country, 1L);
        country.setCountryName("France");

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        Optional<Country> foundCountry = countryService.getCountryById(1L);

        assertTrue(foundCountry.isPresent());
        assertEquals("France", foundCountry.get().getCountryName());
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCountryByIdNotFound() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Country> foundCountry = countryService.getCountryById(1L);

        assertFalse(foundCountry.isPresent());
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCountries() throws Exception {
        Country country1 = new Country();
        setId(country1, 1L);
        country1.setCountryName("France");

        Country country2 = new Country();
        setId(country2, 2L);
        country2.setCountryName("Germany");

        when(countryRepository.findAll()).thenReturn(Arrays.asList(country1, country2));

        List<Country> allCountries = countryService.getAllCountries();

        assertNotNull(allCountries);
        assertEquals(2, allCountries.size());
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCountry() throws Exception {
        Country country = new Country();
        setId(country, 1L);
        country.setCountryName("France");

        Country updatedCountry = new Country();
        updatedCountry.setCountryName("Germany");

        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        when(countryRepository.save(country)).thenReturn(country);

        Country result = countryService.updateCountry(1L, updatedCountry);

        assertNotNull(result);
        assertEquals("Germany", result.getCountryName());
        verify(countryRepository, times(1)).findById(1L);
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    void testUpdateCountryNotFound() {
        Country updatedCountry = new Country();
        updatedCountry.setCountryName("Germany");

        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        Country result = countryService.updateCountry(1L, updatedCountry);

        assertNull(result);
        verify(countryRepository, times(1)).findById(1L);
        verify(countryRepository, times(0)).save(updatedCountry);
    }

    @Test
    void testDeleteCountry() {
        doNothing().when(countryRepository).deleteById(1L);

        countryService.deleteCountry(1L);

        verify(countryRepository, times(1)).deleteById(1L);
    }
}
