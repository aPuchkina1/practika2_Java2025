package com.puchkina.restaurant_rating.dto.restaurant;

import java.math.BigDecimal;

public record RestaurantResponseDto(
        Long id,
        String name,
        String description,
        String cuisineType,
        BigDecimal averageCheck,
        BigDecimal rating
) {}
