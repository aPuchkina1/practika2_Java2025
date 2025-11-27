package com.puchkina.restaurant_rating.controller;

import com.puchkina.restaurant_rating.dto.review.ReviewRequestDto;
import com.puchkina.restaurant_rating.dto.review.ReviewResponseDto;
import com.puchkina.restaurant_rating.entity.Review;
import com.puchkina.restaurant_rating.mapper.ReviewMapper;
import com.puchkina.restaurant_rating.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

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
                .filter(r -> r.getVisitorId().equals(visitorId)
                        && r.getRestaurantId().equals(restaurantId))
                .findFirst()
                .map(reviewMapper::toResponseDto)
                .orElse(null);
    }

    @PostMapping
    public ReviewResponseDto create(@RequestBody @Valid ReviewRequestDto dto) {
        Review review = reviewMapper.toEntity(dto);
        reviewService.save(review);
        return reviewMapper.toResponseDto(review);
    }

    @PutMapping("/{visitorId}/{restaurantId}")
    public ReviewResponseDto update(@PathVariable Long visitorId,
                                    @PathVariable Long restaurantId,
                                    @RequestBody @Valid ReviewRequestDto dto) {
        reviewService.findAll().stream()
                .filter(r -> r.getVisitorId().equals(visitorId)
                        && r.getRestaurantId().equals(restaurantId))
                .findFirst()
                .ifPresent(reviewService::remove);

        Review updated = reviewMapper.toEntity(dto);
        reviewService.save(updated);

        return reviewMapper.toResponseDto(updated);
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    public void delete(@PathVariable Long visitorId,
                       @PathVariable Long restaurantId) {
        reviewService.findAll().stream()
                .filter(r -> r.getVisitorId().equals(visitorId)
                        && r.getRestaurantId().equals(restaurantId))
                .findFirst()
                .ifPresent(reviewService::remove);
    }
}
