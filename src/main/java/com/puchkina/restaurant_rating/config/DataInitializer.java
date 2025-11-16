package com.puchkina.restaurant_rating.config;

import com.puchkina.restaurant_rating.entity.CuisineType;
import com.puchkina.restaurant_rating.entity.Restaurant;
import com.puchkina.restaurant_rating.entity.Review;
import com.puchkina.restaurant_rating.entity.Visitor;
import com.puchkina.restaurant_rating.service.RestaurantService;
import com.puchkina.restaurant_rating.service.ReviewService;
import com.puchkina.restaurant_rating.service.VisitorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @PostConstruct
    public void initData() {
        System.out.println("\nТЕСТ: DataInitializer");

        Visitor v1 = new Visitor(1L, "Оксана", 25, "F");
        Visitor v2 = new Visitor(2L, null, 30, "F"); // анонимный
        Visitor v3 = new Visitor(3L, "Максим", 19, "M");

        visitorService.save(v1);
        visitorService.save(v2);
        visitorService.save(v3);

        Restaurant r1 = new Restaurant(
                1L,
                "MAMMAMIA!",
                "Cause I'm ITALIANO",
                CuisineType.ИТАЛЬЯНСКАЯ,
                new BigDecimal("1200"),
                BigDecimal.ZERO
        );

        Restaurant r2 = new Restaurant(
                2L,
                "ChinaTown",
                "ПО лапшичке?",
                CuisineType.КИТАЙСКАЯ,
                new BigDecimal("900"),
                BigDecimal.ZERO
        );

        restaurantService.save(r1);
        restaurantService.save(r2);

        Review rev1 = new Review(1L, 1L, 5, "Отлично!");
        Review rev2 = new Review(2L, 1L, 4, "Хорошо, но шумно");
        Review rev3 = new Review(3L, 2L, 3, "Лапша супер! Персонал неприятный!");

        reviewService.save(rev1);
        reviewService.save(rev2);
        reviewService.save(rev3);

        System.out.println("Посетители: " + visitorService.findAll());
        System.out.println("Рестораны: " + restaurantService.findAll());
        System.out.println("Оценки: " + reviewService.findAll());

        System.out.println("КОНЕЦ ТЕСТА: DataInitializer\n");
    }
}