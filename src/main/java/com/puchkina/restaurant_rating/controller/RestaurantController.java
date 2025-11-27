package com.puchkina.restaurant_rating.controller;

import com.puchkina.restaurant_rating.dto.restaurant.RestaurantRequestDto;
import com.puchkina.restaurant_rating.dto.restaurant.RestaurantResponseDto;
import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.mapper.RestaurantMapper;
import com.puchkina.restaurant_rating.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    private final AtomicLong idGenerator = new AtomicLong(10);

    @GetMapping
    public List<RestaurantResponseDto> getAll() {
        return restaurantService.findAll().stream()
                .map(restaurantMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public RestaurantResponseDto create(@RequestBody @Valid RestaurantRequestDto dto) {
        Long newId = idGenerator.incrementAndGet();
        Restaurant restaurant = restaurantMapper.toEntity(dto, newId);
        restaurant.setRating(null);
        restaurantService.save(restaurant);
        return restaurantMapper.toResponseDto(restaurant);
    }

    @PutMapping("/{id}")
    public RestaurantResponseDto update(@PathVariable Long id,
                                        @RequestBody @Valid RestaurantRequestDto dto) {
        restaurantService.findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .ifPresent(restaurantService::remove);

        Restaurant updated = restaurantMapper.toEntity(dto, id);
        restaurantService.save(updated);

        return restaurantMapper.toResponseDto(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        restaurantService.findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .ifPresent(restaurantService::remove);
    }
}
