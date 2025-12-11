package com.puchkina.restaurant_rating.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDto(
        @NotNull(message = "ID посетителя обязателен")
        Long visitorId,

        @NotNull(message = "ID ресторана обязателен")
        Long restaurantId,

        @Min(value = 1, message = "Оценка должна быть от 1 до 5")
        @Max(value = 5, message = "Оценка должна быть от 1 до 5")
        int score,

        String text
) {}