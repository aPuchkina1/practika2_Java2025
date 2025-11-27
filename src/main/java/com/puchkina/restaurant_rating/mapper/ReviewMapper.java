package com.puchkina.restaurant_rating.mapper;

import com.puchkina.restaurant_rating.dto.review.ReviewRequestDto;
import com.puchkina.restaurant_rating.dto.review.ReviewResponseDto;
import com.puchkina.restaurant_rating.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequestDto dto) {
        return new Review(
                dto.visitorId(),
                dto.restaurantId(),
                dto.score(),
                dto.text()
        );
    }

    public ReviewResponseDto toResponseDto(Review review) {
        return new ReviewResponseDto(
                review.getVisitorId(),
                review.getRestaurantId(),
                review.getScore(),
                review.getText()
        );
    }
}