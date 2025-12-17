package com.puchkina.restaurant_rating.service;

import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void save_delegatesToRepository() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant result = restaurantService.save(restaurant);

        assertSame(restaurant, result);
        verify(restaurantRepository).save(restaurant);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void remove_delegatesToRepository() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        restaurantService.remove(restaurant);

        verify(restaurantRepository).delete(restaurant);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void findAll_returnsRepositoryResult() {
        List<Restaurant> expected = List.of(new Restaurant(), new Restaurant());
        when(restaurantRepository.findAll()).thenReturn(expected);

        List<Restaurant> result = restaurantService.findAll();

        assertSame(expected, result);
        verify(restaurantRepository).findAll();
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void findById_whenExists_returnsEntity() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(5L);
        when(restaurantRepository.findById(5L)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.findById(5L);

        assertSame(restaurant, result);
        verify(restaurantRepository).findById(5L);
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    void findById_whenMissing_returnsNull() {
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        Restaurant result = restaurantService.findById(999L);

        assertNull(result);
        verify(restaurantRepository).findById(999L);
        verifyNoMoreInteractions(restaurantRepository);
    }
}
