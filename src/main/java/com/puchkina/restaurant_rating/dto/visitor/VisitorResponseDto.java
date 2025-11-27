package com.puchkina.restaurant_rating.dto.visitor;

public record VisitorResponseDto(
        Long id,
        String name,
        int age,
        String gender
) {}