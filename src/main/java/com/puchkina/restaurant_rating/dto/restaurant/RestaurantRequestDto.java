package com.puchkina.restaurant_rating.dto.restaurant;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RestaurantRequestDto(
        @NotBlank(message = "Название ресторана обязательно")
        String name,

        String description,

        @NotBlank(message = "Тип кухни обязателен")
        String cuisineType,

        @NotNull(message = "Средний чек обязателен")
        @DecimalMin(value = "1.0", message = "Средний чек должен быть больше 0")
        BigDecimal averageCheck
) {}
