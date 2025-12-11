package com.puchkina.restaurant_rating.controller;

import com.puchkina.restaurant_rating.dto.review.ReviewRequestDto;
import com.puchkina.restaurant_rating.dto.review.ReviewResponseDto;
import com.puchkina.restaurant_rating.entity.Review;
import com.puchkina.restaurant_rating.mapper.ReviewMapper;
import com.puchkina.restaurant_rating.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.puchkina.restaurant_rating.service.RestaurantService;
import com.puchkina.restaurant_rating.service.VisitorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final RestaurantService restaurantService;
    private final VisitorService visitorService;

    @GetMapping
    public List<ReviewResponseDto> getAll() {
        return reviewService.findAll().stream()
                .map(reviewMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    public ReviewResponseDto getOne(@PathVariable Long visitorId,
                                    @PathVariable Long restaurantId) {
        return reviewService.findAll().stream()
                .filter(r -> r.getVisitor().getId().equals(visitorId)
                        && r.getRestaurant().getId().equals(restaurantId))
                .findFirst()
                .map(reviewMapper::toResponseDto)
                .orElse(null);
    }

    @PostMapping
    public ReviewResponseDto create(@RequestBody @Valid ReviewRequestDto dto) {
        var restaurant = restaurantService.findById(dto.restaurantId());
        var visitor = visitorService.findById(dto.visitorId());

        Review review = reviewMapper.toEntity(dto, restaurant, visitor);
        Review saved = reviewService.save(review);

        return reviewMapper.toResponseDto(saved);
    }


    @PutMapping("/{visitorId}/{restaurantId}")
    public ReviewResponseDto update(@PathVariable Long visitorId,
                                    @PathVariable Long restaurantId,
                                    @RequestBody @Valid ReviewRequestDto dto) {

        reviewService.findAll().stream()
                .filter(r -> r.getVisitor().getId().equals(visitorId)
                        && r.getRestaurant().getId().equals(restaurantId))
                .findFirst()
                .ifPresent(reviewService::remove);

        var restaurant = restaurantService.findById(dto.restaurantId());
        var visitor = visitorService.findById(dto.visitorId());

        Review updated = reviewMapper.toEntity(dto, restaurant, visitor);
        Review saved = reviewService.save(updated);

        return reviewMapper.toResponseDto(saved);
    }


    @DeleteMapping("/{visitorId}/{restaurantId}")
    public void delete(@PathVariable Long visitorId,
                       @PathVariable Long restaurantId) {
        reviewService.findAll().stream()
                .filter(r -> r.getVisitor().getId().equals(visitorId)
                        && r.getRestaurant().getId().equals(restaurantId))
                .findFirst()
                .ifPresent(reviewService::remove);
    }
}
