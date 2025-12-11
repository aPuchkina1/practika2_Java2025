package com.puchkina.restaurant_rating.mapper;

import com.puchkina.restaurant_rating.dto.review.ReviewRequestDto;
import com.puchkina.restaurant_rating.dto.review.ReviewResponseDto;
import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.entity.Review;
import com.puchkina.restaurant_rating.entity.Visitor;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequestDto dto, Restaurant restaurant, Visitor visitor) {
        Review review = new Review();
        review.setScore(dto.score());
        review.setText(dto.text());
        review.setRestaurant(restaurant);
        review.setVisitor(visitor);
        return review;
    }

    public ReviewResponseDto toResponseDto(Review review) {
        return new ReviewResponseDto(
                review.getRestaurant().getId(),
                review.getVisitor().getId(),
                review.getScore(),
                review.getText()
        );
    }
}