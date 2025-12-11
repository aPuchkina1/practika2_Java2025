package com.puchkina.restaurant_rating.dto.review;

public record ReviewResponseDto(
        Long visitorId,
        Long restaurantId,
        int score,
        String text
) {}
