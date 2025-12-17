package com.puchkina.restaurant_rating.service;

import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.entity.Review;
import com.puchkina.restaurant_rating.repository.RestaurantRepository;
import com.puchkina.restaurant_rating.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void save_recalculatesRestaurantRating_whenRestaurantExistsAndHasReviews() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);

        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setScore(5);

        when(reviewRepository.save(review)).thenReturn(review);
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));

        Review r1 = new Review();
        r1.setScore(5);
        Review r2 = new Review();
        r2.setScore(3);
        when(reviewRepository.findByRestaurantId(10L)).thenReturn(List.of(r1, r2));

        reviewService.save(review);

        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository, atLeastOnce()).save(restaurantCaptor.capture());

        Restaurant savedRestaurant = restaurantCaptor.getValue();
        assertNotNull(savedRestaurant.getRating());
        assertEquals(0, savedRestaurant.getRating().compareTo(new BigDecimal("4.00")));

        verify(reviewRepository).save(review);
        verify(restaurantRepository).findById(10L);
        verify(reviewRepository).findByRestaurantId(10L);
    }

    @Test
    void remove_setsRatingToZero_whenNoReviewsRemain() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);

        Review review = new Review();
        review.setRestaurant(restaurant);

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.findByRestaurantId(10L)).thenReturn(List.of());

        reviewService.remove(review);

        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).save(restaurantCaptor.capture());

        Restaurant savedRestaurant = restaurantCaptor.getValue();
        assertNotNull(savedRestaurant.getRating());
        assertEquals(0, savedRestaurant.getRating().compareTo(BigDecimal.ZERO));

        verify(reviewRepository).delete(review);
        verify(restaurantRepository).findById(10L);
        verify(reviewRepository).findByRestaurantId(10L);
    }

    @Test
    void save_doesNothingIfRestaurantMissing() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(123L);

        Review review = new Review();
        review.setRestaurant(restaurant);

        when(reviewRepository.save(review)).thenReturn(review);
        when(restaurantRepository.findById(123L)).thenReturn(Optional.empty());

        reviewService.save(review);

        verify(reviewRepository).save(review);
        verify(restaurantRepository).findById(123L);
        verify(reviewRepository, never()).findByRestaurantId(anyLong());
        verify(restaurantRepository, never()).save(any());
    }
}
