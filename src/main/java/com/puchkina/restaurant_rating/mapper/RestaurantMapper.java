package com.puchkina.restaurant_rating.mapper;

import com.puchkina.restaurant_rating.dto.restaurant.RestaurantRequestDto;
import com.puchkina.restaurant_rating.dto.restaurant.RestaurantResponseDto;
import com.puchkina.restaurant_rating.entity.CuisineType;
import com.puchkina.restaurant_rating.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public Restaurant toEntity(RestaurantRequestDto dto, Long id) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(dto.name());
        restaurant.setDescription(dto.description());
        restaurant.setCuisineType(CuisineType.valueOf(dto.cuisineType()));
        restaurant.setAverageCheck(dto.averageCheck());
        return restaurant;
    }

    public Restaurant toEntity(RestaurantRequestDto dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.name());
        restaurant.setDescription(dto.description());
        restaurant.setCuisineType(CuisineType.valueOf(dto.cuisineType()));
        restaurant.setAverageCheck(dto.averageCheck());
        return restaurant;
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