package com.puchkina.restaurant_rating.config;

import com.puchkina.restaurant_rating.service.RestaurantService;
import com.puchkina.restaurant_rating.service.ReviewService;
import com.puchkina.restaurant_rating.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @Override
    public void run(String... args) {
        System.out.println("\nТЕСТ: AppRunner");

        System.out.println("Посетители: " + visitorService.findAll());
        System.out.println("Рестораны: " + restaurantService.findAll());
        System.out.println("Оценки: " + reviewService.findAll());

        System.out.println("КОНЕЦ ТЕСТА: AppRunner\n");
    }
}