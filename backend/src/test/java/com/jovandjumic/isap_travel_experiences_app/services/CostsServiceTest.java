package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.Costs;
import com.jovandjumic.isap_travel_experiences_app.repositories.CostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CostsServiceTest {

    @Mock
    private CostsRepository costsRepository;

    @InjectMocks
    private CostsService costsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void setId(Object entity, Long idValue) throws Exception {
        Field idField = entity.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, idValue);
    }

    @Test
    void testCreateCosts() {
        Costs costs = new Costs();
        costs.setTravelCost(500);

        when(costsRepository.save(costs)).thenReturn(costs);

        Costs createdCosts = costsService.createCosts(costs);

        assertNotNull(createdCosts);
        assertEquals(500, createdCosts.getTravelCost());
        verify(costsRepository, times(1)).save(costs);
    }

    @Test
    void testGetCostsById() throws Exception {
        Costs costs = new Costs();
        setId(costs, 1L);
        costs.setTravelCost(500);

        when(costsRepository.findById(1L)).thenReturn(Optional.of(costs));

        Optional<Costs> foundCosts = costsService.getCostsById(1L);

        assertTrue(foundCosts.isPresent());
        assertEquals(500, foundCosts.get().getTravelCost());
        verify(costsRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCostsByIdNotFound() {
        when(costsRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Costs> foundCosts = costsService.getCostsById(1L);

        assertFalse(foundCosts.isPresent());
        verify(costsRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCosts() throws Exception {
        Costs costs1 = new Costs();
        setId(costs1, 1L);
        costs1.setTravelCost(500);

        Costs costs2 = new Costs();
        setId(costs2, 2L);
        costs2.setTravelCost(700);

        when(costsRepository.findAll()).thenReturn(Arrays.asList(costs1, costs2));

        List<Costs> allCosts = costsService.getAllCosts();

        assertNotNull(allCosts);
        assertEquals(2, allCosts.size());
        verify(costsRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCosts() throws Exception {
        Costs costs = new Costs();
        setId(costs, 1L);
        costs.setTravelCost(500);

        Costs updatedCosts = new Costs();
        updatedCosts.setTravelCost(700);

        when(costsRepository.findById(1L)).thenReturn(Optional.of(costs));
        when(costsRepository.save(costs)).thenReturn(costs);

        Costs result = costsService.updateCosts(1L, updatedCosts);

        assertNotNull(result);
        assertEquals(700, result.getTravelCost());
        verify(costsRepository, times(1)).findById(1L);
        verify(costsRepository, times(1)).save(costs);
    }

    @Test
    void testUpdateCostsNotFound() {
        Costs updatedCosts = new Costs();
        updatedCosts.setTravelCost(700);

        when(costsRepository.findById(1L)).thenReturn(Optional.empty());

        Costs result = costsService.updateCosts(1L, updatedCosts);

        assertNull(result);
        verify(costsRepository, times(1)).findById(1L);
        verify(costsRepository, times(0)).save(updatedCosts);
    }

    @Test
    void testDeleteCosts() {
        doNothing().when(costsRepository).deleteById(1L);

        costsService.deleteCosts(1L);

        verify(costsRepository, times(1)).deleteById(1L);
    }
}
