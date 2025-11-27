package com.puchkina.restaurant_rating.mapper;

import com.puchkina.restaurant_rating.dto.restaurant.RestaurantRequestDto;
import com.puchkina.restaurant_rating.dto.restaurant.RestaurantResponseDto;
import com.puchkina.restaurant_rating.entity.CuisineType;
import com.puchkina.restaurant_rating.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public Restaurant toEntity(RestaurantRequestDto dto, Long id) {
        return new Restaurant(
                id,
                dto.name(),
                dto.description(),
                CuisineType.valueOf(dto.cuisineType()),
                dto.averageCheck(),
                null
        );
    }

    public Restaurant toEntity(RestaurantRequestDto dto) {
        return new Restaurant(
                null,
                dto.name(),
                dto.description(),
                CuisineType.valueOf(dto.cuisineType()),
                dto.averageCheck(),
                null
        );
    }

    public RestaurantResponseDto toResponseDto(Restaurant restaurant) {
        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getCuisineType().name(),
                restaurant.getAverageCheck(),
                restaurant.getRating()
        );
    }
}