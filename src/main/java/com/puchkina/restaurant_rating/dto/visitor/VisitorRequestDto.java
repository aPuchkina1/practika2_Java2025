package com.puchkina.restaurant_rating.dto.visitor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VisitorRequestDto(
        String name,

        @Min(value = 0, message = "Возраст не может быть отрицательным")
        int age,

        @NotNull(message = "Пол обязателен")
        @Size(min = 1, message = "Пол не может быть пустым")
        String gender
) {}
