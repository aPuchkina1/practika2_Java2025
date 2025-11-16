package com.puchkina.restaurant_rating.service;

import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.entity.Review;
import com.puchkina.restaurant_rating.repository.RestaurantRepository;
import com.puchkina.restaurant_rating.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    public void save(Review review) {
        reviewRepository.save(review);
        recalculateRestaurantRating(review.getRestaurantId());
    }

    public void remove(Review review) {
        reviewRepository.remove(review);
        recalculateRestaurantRating(review.getRestaurantId());
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(restaurantId))
                .findFirst()
                .orElse(null);

        if (restaurant == null) {
            return;
        }

        List<Review> reviewsForRestaurant = reviewRepository.findAll().stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());

        if (reviewsForRestaurant.isEmpty()) {
            restaurant.setRating(BigDecimal.ZERO);
            return;
        }

        int sum = reviewsForRestaurant.stream()
                .mapToInt(Review::getScore)
                .sum();

        BigDecimal average = BigDecimal.valueOf(sum)
                .divide(BigDecimal.valueOf(reviewsForRestaurant.size()), 2, RoundingMode.HALF_UP);

        restaurant.setRating(average);
    }
}